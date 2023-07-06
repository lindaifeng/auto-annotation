package cn.auto.annotation;

import cn.auto.enums.RateLimitTypeEnum;

import java.lang.annotation.*;

/** 限流注解
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {
    /**
     * 限流key
     */
    String key() default "rate_limit_key:";

    /**
     * 限流时间,单位秒
     */
    int time() default 60;

    /**
     * 限流次数
     */
    int count() default 100;

    /**
     * 限流类型
     */
    RateLimitTypeEnum limitType() default RateLimitTypeEnum.GLOBAL;
}