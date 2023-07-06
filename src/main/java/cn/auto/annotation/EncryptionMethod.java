package cn.auto.annotation;

import java.lang.annotation.*;

/** 加密方法标记注解 标记需加密的接口
 *  与@EncryptionFields连用
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EncryptionMethod {
}
