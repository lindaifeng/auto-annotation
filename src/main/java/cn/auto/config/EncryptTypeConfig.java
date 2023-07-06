package cn.auto.config;

import cn.auto.enums.EncryptTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "auto.encrypt")
public class EncryptTypeConfig {
    /**
     * 加密方式
     */
    EncryptTypeEnum encryptType = EncryptTypeEnum.PLAIN;
    /**
     * 请求头公共密钥名称（非对称加密使用）
     */
    String pubKeyName;
    /**
     * 私有密钥key
     */
    String priKey;

}
