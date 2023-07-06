package cn.auto.core.dynamic;

import cn.auto.annotation.DynamicDb;
import cn.auto.enums.DbType;
import cn.auto.exception.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Slf4j
@Aspect
@Component
public class DynamicDataSourceAop {


    @Around(value = "@annotation(dynamicDb)")
    public Object around(ProceedingJoinPoint jp, DynamicDb dynamicDb) throws ServerException {
        try {
            MethodSignature signature = (MethodSignature) jp.getSignature();
            Method method = signature.getMethod();
            DynamicDb annotation = method.getAnnotation(DynamicDb.class);
            DbType dbType = annotation.value();
            DbThreadLocal.setType(dbType);
            int i = 1/0;
            return jp.proceed();
        } catch (Throwable e) {
            log.error("处理异常", e);
            throw new ServerException("程序运行异常", e);
        } finally {
            //清理本地线程数据源，避免上下文数据源逻辑混乱
            DbThreadLocal.cleanType();
        }

    }
}
