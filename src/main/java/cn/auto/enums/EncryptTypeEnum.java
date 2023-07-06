package cn.auto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 加密类型枚举
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Getter
@AllArgsConstructor
public enum EncryptTypeEnum {
    /**
     * 明文
     */
    PLAIN("PLAIN","明文"),
    /**
     * DES加密算法
     */
    DES("DES","DES加密算法"),
    /**
     * AES加密算法
     */
    AES("AES","AES加密算法"),
    /**
     * SM2加密算法
     */
    SM2("SM2","SM2加密算法"),
    /**
     * SM4加密算法
     */
    SM4("SM4","SM4加密算法"),

    ;

    private final String value;
    private final String desc;
}
