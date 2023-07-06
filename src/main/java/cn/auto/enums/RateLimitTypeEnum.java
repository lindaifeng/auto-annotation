package cn.auto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Getter
@AllArgsConstructor
public enum RateLimitTypeEnum {
    /**
     * 全局限流
     */
    GLOBAL("GLOBAL","全局限流"),
    /**
     * IP限流
     */
    IP("IP","IP限流"),
    ;

    private final String value;
    private final String desc;

}
