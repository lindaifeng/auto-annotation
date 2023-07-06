package cn.auto.annotation;

import cn.auto.enums.DbType;

import java.lang.annotation.*;

/** 动态数据源注解
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DynamicDb {
    /**
     * 切换数据库源
     */
    DbType value() default DbType.PRIMARY_DB;
}
