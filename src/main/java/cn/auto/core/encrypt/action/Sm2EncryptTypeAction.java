package cn.auto.core.encrypt.action;

import cn.auto.config.EncryptTypeConfig;
import cn.auto.enums.EncryptTypeEnum;
import cn.auto.utils.Sm2Utils;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 * 私钥:5b27a37cf7305146c062f4b59b257d1888e9f5e65c2017b9c48556aaf27d9f5b
 * 公钥:02bb7b3cf323f53921f3cf2d05131144d7378c10ceb2fa5bfe3756643722f252a6
 */
@Component
public class Sm2EncryptTypeAction implements IEncryptTypeStrategy{

    @Resource
    private EncryptTypeConfig encryptTypeConfig;

    @Override
    public EncryptTypeEnum isSupport() {
        return EncryptTypeEnum.SM2;
    }

    @Override
    public String encrypt(String str, String key) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String publicKey = request.getHeader(encryptTypeConfig.getPubKeyName());
        str = Sm2Utils.encrypt(publicKey, str);
        return str;
    }

    @Override
    public String decrypt(String str, String key) {
        str = Sm2Utils.decrypt(encryptTypeConfig.getPriKey(), str);
        return str;
    }
}
