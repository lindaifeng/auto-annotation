package cn.auto.core.encrypt.action;

import cn.auto.config.EncryptTypeConfig;
import cn.auto.enums.EncryptTypeEnum;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Component
public class AesEncryptTypeAction implements IEncryptTypeStrategy{

   private AES aes;

    @Resource
    private EncryptTypeConfig encryptTypeConfig;

    @PostConstruct
    private void constructAes(){
        SecretKey secretKey = SecureUtil.generateKey(EncryptTypeEnum.AES.getValue(),
                encryptTypeConfig.getPriKey().getBytes(StandardCharsets.UTF_8));
         this.aes = SecureUtil.aes(secretKey.getEncoded());
    }

    @Override
    public EncryptTypeEnum isSupport() {
        return EncryptTypeEnum.AES;
    }

    @Override
    public String encrypt(String str, String key) {
        str = aes.encryptBase64(str);
        return str;
    }

    @Override
    public String decrypt(String str, String key) {
        str = aes.decryptStr(str);
        return str;
    }

    public static void main(String[] args) {
        SecretKey aes = KeyUtil.generateKey(EncryptTypeEnum.AES.getValue(),128);
        String key = HexUtil.encodeHexStr(aes.getEncoded());
        System.out.println(key);
    }
}
