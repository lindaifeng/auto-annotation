package cn.auto.annotation;

import java.lang.annotation.*;

/** 登录校验注解
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginRequired {
    
}