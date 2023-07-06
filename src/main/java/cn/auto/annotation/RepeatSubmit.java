package cn.auto.annotation;

import java.lang.annotation.*;

/** 防重复提交注解
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmit {
    /**
     * 间隔时间(ms)，小于此时间视为重复提交
     */
     int interval() default 5000;

    /**
     * 提示消息
     */
     String message() default "不允许重复提交，请稍后再试";
}