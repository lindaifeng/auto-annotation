package cn.auto.annotation;

import cn.auto.core.validated.MockValidatorImpl;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/** 自定义校验注解
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = MockValidatorImpl.class)
public @interface MockValidator {
    /**
     * 校验响应信息
     */
    String message() default "xxx校验不通过";

    /**
     * 校验分组
     */
    Class<?>[] groups() default {};

    /**
     * 自定义校验负载信息
     */
    Class<? extends Payload>[] payload() default {};

}