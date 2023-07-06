package cn.auto.annotation;

import java.lang.annotation.*;

/** 操作日志注解集
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperateLogs {

    OperateLog[] value();
}
