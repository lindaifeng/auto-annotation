package cn.auto.core.encrypt.action;

import cn.auto.config.EncryptTypeConfig;
import cn.auto.enums.EncryptTypeEnum;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.DES;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Component
public class DesEncryptTypeAction implements IEncryptTypeStrategy{

    @Resource
    private EncryptTypeConfig encryptTypeConfig;

    private DES des;

    @PostConstruct
    private void constructAes(){
        SecretKey secretKey = SecureUtil.generateKey(EncryptTypeEnum.DES.getValue(),
                encryptTypeConfig.getPriKey().getBytes(StandardCharsets.UTF_8));
        this.des = SecureUtil.des(secretKey.getEncoded());
    }

    @Override
    public EncryptTypeEnum isSupport() {
        return EncryptTypeEnum.DES;
    }

    @Override
    public String encrypt(String str, String key) {
        return des.encryptBase64(str);
    }

    @Override
    public String decrypt(String str, String key) {
        return des.decryptStr(str);
    }

}
