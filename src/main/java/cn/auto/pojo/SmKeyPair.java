package cn.auto.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 1. @description: 密钥对传输对象
 * @author ldf
 */
@Data
@AllArgsConstructor
public class SmKeyPair {

    /**
     * 私钥
     */
    private String priKey;
    /**
     * 公钥
     */
    private String pubKey;

}