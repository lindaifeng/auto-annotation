package cn.auto.core.operate.handler;

import org.springframework.core.NamedThreadLocal;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
public class OperateLogContext {

    private static final ThreadLocal<StandardEvaluationContext> THREAD_LOCAL = new NamedThreadLocal<>("ThreadLocal-StandardEvaluationContext");

    public static final String RESULT = "result";

    public static StandardEvaluationContext getContext() {
        return THREAD_LOCAL.get() == null ? new StandardEvaluationContext() : THREAD_LOCAL.get();
    }

    public static void setContext(String key, Object value) {
        StandardEvaluationContext context = getContext();
        context.setVariable(key, value);
        THREAD_LOCAL.set(context);
    }

    public static ThreadLocal<StandardEvaluationContext> getThreadLocal() {
        return THREAD_LOCAL;
    }

    public static void clearContext() {
        THREAD_LOCAL.remove();
    }
}
