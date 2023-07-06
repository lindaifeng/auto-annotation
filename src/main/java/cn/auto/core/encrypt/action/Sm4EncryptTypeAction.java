package cn.auto.core.encrypt.action;

import cn.auto.config.EncryptTypeConfig;
import cn.auto.enums.EncryptTypeEnum;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SM4;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Component
public class Sm4EncryptTypeAction implements IEncryptTypeStrategy{

    @Resource
    private EncryptTypeConfig encryptTypeConfig;

    @Override
    public EncryptTypeEnum isSupport() {
        return EncryptTypeEnum.SM4;
    }

    private SM4 sm4;

    @PostConstruct
    private void constructAes(){
        this.sm4 = SmUtil.sm4(encryptTypeConfig.getPriKey().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String encrypt(String str, String key) {
        return sm4.encryptBase64(str);
    }

    @Override
    public String decrypt(String str, String key) {
        return sm4.decryptStr(str);
    }

    public static void main(String[] args) {
        SM4 sm4 = SmUtil.sm4("13b2d3f047502666".getBytes(StandardCharsets.UTF_8));
        String encryptBase64 = sm4.encryptBase64("年龄");
        System.out.println(encryptBase64);
    }
}
