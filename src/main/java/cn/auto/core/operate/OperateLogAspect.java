package cn.auto.core.operate;

import cn.auto.annotation.OperateLog;
import cn.auto.constants.ConditionalConstants;
import cn.auto.core.operate.action.IOperateLogOutput;
import cn.auto.core.operate.handler.OperateLogContext;
import cn.auto.exception.ServerException;
import cn.auto.pojo.OperateLogDTO;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Slf4j
@Aspect
@Configuration
@ConditionalOnProperty(prefix = ConditionalConstants.AUTO_ENABLE,name = ConditionalConstants.OPERATE_LOG,havingValue = ConditionalConstants.ENABLE_TRUE)
public class OperateLogAspect {

    private final SpelExpressionParser parser = new SpelExpressionParser();

    @Resource
    private IOperateLogOutput operateLogOutput;

    @Around("@annotation(cn.auto.annotation.OperateLogs) || @annotation(cn.auto.annotation.OperateLog)")
    public Object around(ProceedingJoinPoint jp) throws Throwable {
        List<OperateLogDTO> operateLogList = new ArrayList<>();
        Object result;
        OperateLog[] annotations;
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        try {
            //拦截注解
            annotations = method.getAnnotationsByType(OperateLog.class);

            //方法执行前解析参数
            annotationResoleExpression(Boolean.FALSE, jp, operateLogList, null, annotations, signature);

            //方法执行
            result = jp.proceed();

            //方法执行后解析参数
            annotationResoleExpression(Boolean.TRUE, jp, operateLogList, result, annotations, signature);

        } catch (Throwable throwable) {
            //捕获到jp.proceed()方法执行异常，补齐记录方法执行后日志信息（方法未成功执行，后置日志可不收集）
//            annotationResoleExpression(jp, operateLogList, result, annotations, signature);

            operateLogList.forEach(x -> x.setException(throwable.getMessage()));

            throw new ServerException("方法执行异常", throwable);

        } finally {
            //输出记录日志信息
            operateLogOutput.outPut(operateLogList);
        }
        return result;
    }


    private void annotationResoleExpression(Boolean isAfter, ProceedingJoinPoint jp, List<OperateLogDTO> operateLogList,
                                            Object result, OperateLog[] annotations, MethodSignature signature) {
        String errorStr = "初始化方法执行前后异常内容";
        try {
            for (OperateLog annotation : annotations) {
                if (Boolean.FALSE.equals(isAfter) && !annotation.after()) {
                    errorStr = "方法执行前解析参数异常";
                } else if (Boolean.TRUE.equals(isAfter) && annotation.after()) {
                    errorStr = "方法执行后解析参数异常";
                } else {
                    continue;
                }
                setLogContextParam(jp, signature, result);
                OperateLogDTO operateLogDTO = resoleExpression(annotation);
                operateLogList.add(operateLogDTO);
            }
        } catch (Exception e) {
            log.error(errorStr, e);
        }
    }


    private void setLogContextParam(ProceedingJoinPoint jp, MethodSignature signature, Object result) {
        //插入入参
        String[] parameterNames = signature.getParameterNames();
        Object[] args = jp.getArgs();
        for (int i = 0; i < parameterNames.length; i++) {
            OperateLogContext.setContext(parameterNames[i], args[i]);
        }
        //插入结果
        if (!ObjectUtils.isEmpty(result)) {
            OperateLogContext.setContext(OperateLogContext.RESULT, result);
        }
    }

    private OperateLogDTO resoleExpression(OperateLog annotation) {
        StandardEvaluationContext context = OperateLogContext.getContext();
        OperateLogDTO operateLogDTO = OperateLogDTO.builder().build();
        //解析注解el
        String bid = annotation.bid();
        if (StringUtils.hasText(bid)) {
            Expression expression = parser.parseExpression(bid);
            String bidValue = expression.getValue(context, String.class);
            operateLogDTO.setBid(bidValue);
        }

        operateLogDTO.setOperateType(annotation.operateType());

        String message = annotation.message();
        if (StringUtils.hasText(message)) {
            Expression expression = parser.parseExpression(message);
            String messageValue = expression.getValue(context, String.class);
            operateLogDTO.setMessage(messageValue);
        }

        String tag = annotation.tag();
        if (StringUtils.hasText(tag)) {
            Expression expression = parser.parseExpression(tag);
            String tagValue = expression.getValue(context, String.class);
            operateLogDTO.setTag(tagValue);
        }

        String operatorId = annotation.operatorId();
        if (StringUtils.hasText(operatorId)) {
            Expression expression = parser.parseExpression(operatorId);
            String operatorIdValue = expression.getValue(context, String.class);
            operateLogDTO.setOperatorId(operatorIdValue);
        }

        if (annotation.recordResult()) {
            operateLogDTO.setRecordResult(Boolean.TRUE);
            Object resultValue = context.lookupVariable(OperateLogContext.RESULT);
            operateLogDTO.setResult(JSON.toJSONString(resultValue));
        }
        operateLogDTO.setSuccess(Boolean.TRUE);
        //清理上下文
        OperateLogContext.clearContext();

        return operateLogDTO;
    }
}
