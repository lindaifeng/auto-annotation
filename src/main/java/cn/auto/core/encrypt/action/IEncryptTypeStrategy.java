package cn.auto.core.encrypt.action;

import cn.auto.enums.EncryptTypeEnum;

/**
 * 加密接口
 *
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
public interface IEncryptTypeStrategy {
    /**
     * 是否支持该加密方式
     *
     * @return 结果集
     */
    EncryptTypeEnum isSupport();

    /**
     * 加密数据
     *
     * @param str         原始数据
     * @param key         公钥key（非对称加密使用）
     * @return 结果集
     */
    String encrypt(String str, String key);

    /**
     * 解密数据
     *
     * @param str 已加密数据
     * @param key 公钥key（非对称加密使用）
     * @return 结果集
     */
    String decrypt(String str, String key);
}
