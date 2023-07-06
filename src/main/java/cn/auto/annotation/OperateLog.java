package cn.auto.annotation;

import cn.auto.enums.OperateTypeEnum;

import java.lang.annotation.*;

/** 操作日志注解
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(OperateLogs.class)
public @interface OperateLog {

    /**
     * 业务id
     */
    String bid();
    /**
     * 日志标签
     */
    String tag();
    /**
     * 操作类型
     */
    OperateTypeEnum operateType();
    /**
     * 日志消息
     */
    String message() default "";
    /**
     * 操作人员
     */
    String operatorId() default "";
    /**
     * 是否记录结果值
     */
    boolean recordResult() default false;
    /**
     * 结果值
     */
    String result() default "";
    /**
     * 是否在方法执行后记录（默认方法执行前记录）
     */
    boolean after() default false;

}
