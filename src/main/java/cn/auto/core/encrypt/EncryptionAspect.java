package cn.auto.core.encrypt;

import cn.auto.annotation.EncryptionFields;
import cn.auto.annotation.EncryptionMethod;
import cn.auto.config.EncryptTypeConfig;
import cn.auto.constants.ConditionalConstants;
import cn.auto.core.encrypt.action.IEncryptTypeStrategy;
import cn.auto.enums.EncryptTypeEnum;
import cn.auto.exception.ServerException;
import cn.auto.pojo.EncryptTypeDTO;
import cn.hutool.core.text.StrPool;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Slf4j
@Aspect
@Configuration
@ConditionalOnProperty(prefix = ConditionalConstants.AUTO_ENABLE,name = ConditionalConstants.ENCRYPTION_DATA,havingValue =ConditionalConstants.ENABLE_TRUE)
public class EncryptionAspect {

    @Resource
    private EncryptTypeConfig encryptTypeConfig;
    @Resource
    private List<IEncryptTypeStrategy> encryptTypeStrategyList;


    @Around(value = "@annotation(encryptionMethod)")
    public Object aroundFields(ProceedingJoinPoint jp, EncryptionMethod encryptionMethod) throws Throwable {

        //1、方法执行前加密
        //获取参数
        Object[] args = jp.getArgs();
        for (Object arg : args) {
            //获取属性
            Field[] declaredFields = arg.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                //获取属性上的注解
                EncryptionFields encryptionField = declaredField.getAnnotation(EncryptionFields.class);
                if (!ObjectUtils.isEmpty(encryptionField)) {
                    //检查并获取属性值
                    Object value = checkFieldValue(arg, declaredField);
                    if (ObjectUtils.isEmpty(value)){
                        continue;
                    }
                    //是否将接收到的参数解密存储
                    if (encryptionField.decryptStorage()) {
                        decryptData(arg, declaredField, value);
                    }
                    //是否将接收到的参数加密存储
                    if (encryptionField.encryptStorage()) {
                        encryptData(arg, declaredField, value);
                    }
                }
            }
        }
        //2、方法执行
        Object result = jp.proceed();
        //3、方法执行后加解密
        if (!ObjectUtils.isEmpty(result)) {
            Field[] declaredFields = result.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                //获取属性上的注解
                EncryptionFields encryptionField = declaredField.getAnnotation(EncryptionFields.class);
                if (!ObjectUtils.isEmpty(encryptionField)) {
                    //检查并获取属性值
                    Object value = checkFieldValue(result, declaredField);
                    if (ObjectUtils.isEmpty(value)){
                        continue;
                    }
                    //是否将查询到的结果解密返回
                    if (encryptionField.decryptReturn()) {
                        decryptData(result, declaredField, value);
                    }
                    //是否将查询到的结果加密返回
                    if (encryptionField.encryptReturn()) {
                        encryptData(result, declaredField, value);
                    }
                }
            }
        }

        return result;
    }

    /**
     * 检查并获取字段属性值
     *
     * @param obj           参数对象
     * @param declaredField 字段属性对象
     * @return 结果集
     * @throws ServerException        服务异常
     * @throws IllegalAccessException 非法访问异常
     */
    private Object checkFieldValue(Object obj, Field declaredField) throws IllegalAccessException, ServerException {
        declaredField.setAccessible(true);
        String name = declaredField.getName();
        Object value = declaredField.get(obj);
        if (ObjectUtils.isEmpty(value)){
            return null;
        }
        if (!(value instanceof String)) {
            throw new ServerException("参数类型有误非字符类型:" + name);
        }
        return value;
    }

    /**
     * 加密数据（加密根据配置的加密规则操作，并把规则{xxx}拼接）
     *
     * @param obj           参数对象
     * @param declaredField 字段属性对象
     * @param value         属性值
     * @throws ServerException        服务异常
     * @throws IllegalAccessException 非法访问异常
     */
    private void encryptData(Object obj, Field declaredField, Object value) throws ServerException, IllegalAccessException {
        String key = encryptTypeConfig.getPriKey();
        EncryptTypeEnum encryptType = encryptTypeConfig.getEncryptType();
        IEncryptTypeStrategy encryptTypeStrategy = getEncryptTypeStrategy(encryptType);
        String encryptStr = encryptTypeStrategy.encrypt(String.valueOf(value), key);
        String encryptValue = StrPool.DELIM_START + encryptType + StrPool.DELIM_END + encryptStr;
        declaredField.set(obj, encryptValue);
    }

    /**
     * 解密数据 (根据值中{xxx}，获取到加密方式进行解密操作)
     *
     * @param obj           参数对象
     * @param declaredField 字段属性对象
     * @param value         属性值
     * @throws ServerException        服务异常
     * @throws IllegalAccessException 非法访问异常
     */
    private void decryptData(Object obj, Field declaredField, Object value) throws ServerException, IllegalAccessException {
        String key = encryptTypeConfig.getPriKey();
        EncryptTypeDTO encryptTypeDTO = splitEncryptType(String.valueOf(value));
        IEncryptTypeStrategy encryptTypeStrategy = getEncryptTypeStrategy(encryptTypeDTO.getEncryptType());
        String decryptStr = encryptTypeStrategy.decrypt(encryptTypeDTO.getEncryptValue(), key);
        declaredField.set(obj, decryptStr);
    }

    /**
     * 根据加密方式获取加密策略者
     *
     * @param encryptType 加密方式
     * @return 结果集
     * @throws ServerException 服务异常
     */
    private IEncryptTypeStrategy getEncryptTypeStrategy(EncryptTypeEnum encryptType) throws ServerException {
        IEncryptTypeStrategy encryptTypeStrategy = encryptTypeStrategyList.stream()
                .filter(x -> x.isSupport().equals(encryptType))
                .findFirst().orElse(null);
        if (ObjectUtils.isEmpty(encryptTypeStrategy)) {
            throw new ServerException("未找到该加密方式的实现");
        }
        return encryptTypeStrategy;
    }

    /**
     * 获取加密数据中的加密方式（{PLAIN}xxssdfsd）
     *
     * @param str 加密数据
     * @return 结果集
     * @throws ServerException 服务异常
     */
    private EncryptTypeDTO splitEncryptType(String str) throws ServerException {
        String[] split = str.split(StrPool.DELIM_END);
        String encryptType = split[0].substring(1);
        String encryptValue = split[1];
        EncryptTypeEnum encryptTypeEnum = EncryptTypeEnum.valueOf(encryptType);
        if (ObjectUtils.isEmpty(encryptTypeEnum)) {
            throw new ServerException("加密参数类型有误");
        }
        return EncryptTypeDTO.builder().encryptType(encryptTypeEnum).encryptValue(encryptValue).build();
    }
}
