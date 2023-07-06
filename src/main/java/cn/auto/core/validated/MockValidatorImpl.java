package cn.auto.core.validated;

import cn.auto.annotation.MockValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

/** 自定义校验注解实现类
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
public class MockValidatorImpl implements ConstraintValidator<MockValidator,Object> {

    @Override
    public void initialize(MockValidator constraintAnnotation) {
        //初始化方法
        String message = constraintAnnotation.message();
        Class<?>[] groups = constraintAnnotation.groups();
        Class<? extends Payload>[] payload = constraintAnnotation.payload();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        //自定义校验逻辑... true：校验通过  false：校验不通过

        //模拟 被注解标记的参数类型非String
        if (!(o instanceof String)){
            constraintValidatorContext.buildConstraintViolationWithTemplate("此为修改后的message提示消息:[xxx校验不通过]");
            return false;
        }
        return true;
    }
}
