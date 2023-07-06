package cn.auto.core.encrypt.action;

import cn.auto.enums.EncryptTypeEnum;
import org.springframework.stereotype.Component;

/** 明文加密
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Component
public class PlainEncryptTypeAction implements IEncryptTypeStrategy{

    @Override
    public EncryptTypeEnum isSupport() {
        return EncryptTypeEnum.PLAIN;
    }

    @Override
    public String encrypt(String str, String key) {

        return str;
    }

    @Override
    public String decrypt(String str, String key) {
        return str;
    }
}
