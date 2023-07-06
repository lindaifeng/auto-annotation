package cn.auto.annotation;

import java.lang.annotation.*;

/** 数据存储字段加解密注解
 * 与@EncryptionMethod连用
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EncryptionFields {
    /**
     * 是否将接收到的参数解密存储
     */
    boolean decryptStorage() default false;
    /**
     * 是否将接收到的参数加密存储
     */
    boolean encryptStorage() default false;

    /**
     * 是否将查询到的结果解密返回
     */
    boolean decryptReturn() default false;
    /**
     * 是否将查询到的结果加密返回
     */
    boolean encryptReturn() default false;
}
