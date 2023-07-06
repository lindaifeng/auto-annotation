package cn.auto.utils;

import cn.auto.pojo.SmKeyPair;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.BCUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;


import java.security.PublicKey;

/**
 * 1. @description: 国密SM2加解密
 * @author ldf
 * <dependency>
 *   <groupId>org.bouncycastle</groupId>
 *   <artifactId>bcprov-jdk15to18</artifactId>
 *   <version>1.69</version>
 * </dependency>
 * <dependency>
 *   <groupId>cn.hutool</groupId>
 *   <artifactId>hutool-all</artifactId>
 *   <version>5.4.1</version>
 * </dependency>
 */
public class Sm2Utils {

    /**
     * 公钥长度
     */
    private static final int PUBLIC_KEY_LENGTH = 130;
    /**
     * 符号
     */
    private static final String STRING_EQUAL = "=";
    /**
     * 生成公私钥对(默认压缩公钥)
     * @return 结果集
     */
    public static SmKeyPair genKeyPair() {
        SM2 sm2 = SmUtil.sm2();
        //这里会自动生成对应的随机秘钥对
        byte[] privateKey = BCUtil.encodeECPrivateKey(sm2.getPrivateKey());
        //这里默认公钥压缩  公钥的第一个字节用于表示是否压缩 02或者03表示是压缩公钥,04表示未压缩公钥
        byte[] publicKey = BCUtil.encodeECPublicKey(sm2.getPublicKey());
        String priKey = HexUtil.encodeHexStr(privateKey);
        String pubKey = HexUtil.encodeHexStr(publicKey);
        return new SmKeyPair(priKey, pubKey);
    }
    /**
     * SM2加密算法
     * @param publicKey     公钥
     * @param text          数据
     * @return 结果集
     */
    public static String encrypt(String publicKey, String text) {
        ECPublicKeyParameters ecPublicKeyParameters;
        //这里需要根据公钥的长度进行加工
        if (publicKey.length() == PUBLIC_KEY_LENGTH) {
            //这里需要去掉开始第一个字节 第一个字节表示标记
            publicKey = publicKey.substring(2);
            String xhex = publicKey.substring(0, 64);
            String yhex = publicKey.substring(64, 128);
            ecPublicKeyParameters = BCUtil.toSm2Params(xhex, yhex);
        } else {
            PublicKey p = BCUtil.decodeECPoint(publicKey, SmUtil.SM2_CURVE_NAME);
            ecPublicKeyParameters = BCUtil.toParams(p);
        }
        //创建sm2 对象
        SM2 sm2 = new SM2(null, ecPublicKeyParameters);
        // 公钥加密
        return sm2.encryptBcd(text, KeyType.PublicKey);
    }

    /**
     * SM2加密算法
     * @param publicKey     公钥
     * @param text          明文数据
     * @return 结果集
     */
    public static String encrypt(PublicKey publicKey, String text) {
        ECPublicKeyParameters ecPublicKeyParameters = BCUtil.toParams(publicKey);
        //创建sm2 对象
        SM2 sm2 = new SM2(null, ecPublicKeyParameters);
        // 公钥加密
        return sm2.encryptBcd(text, KeyType.PublicKey);
    }

    /**
     * SM2解密算法
     * @param privateKey    私钥
     * @param cipherData    密文数据
     * @return 结果集
     */
    public static String decrypt(String privateKey, String cipherData) {
        ECPrivateKeyParameters ecPrivateKeyParameters = BCUtil.toSm2Params(privateKey);
        //创建sm2 对象
        SM2 sm2 = new SM2(ecPrivateKeyParameters, null);
        // 私钥解密
        return StrUtil.utf8Str(sm2.decryptFromBcd(cipherData, KeyType.PrivateKey));
    }

    /**
     * 私钥签名
     * @param privateKey    私钥
     * @param content       待签名内容
     * @return 结果集
     */
    public static String sign(String privateKey, String content) {
        ECPrivateKeyParameters ecPrivateKeyParameters = BCUtil.toSm2Params(privateKey);
        //创建sm2 对象
        SM2 sm2 = new SM2(ecPrivateKeyParameters, null);
        String sign = sm2.signHex(HexUtil.encodeHexStr(content));
        return sign;
    }

    /**
     * 验证签名
     * @param publicKey     公钥
     * @param content       待签名内容
     * @param sign          签名值
     * @return 结果集
     */
    public static boolean verify(String publicKey, String content, String sign) {
        ECPublicKeyParameters ecPublicKeyParameters;
        //这里需要根据公钥的长度进行加工
        if (publicKey.length() == PUBLIC_KEY_LENGTH) {
            //这里需要去掉开始第一个字节 第一个字节表示标记
            publicKey = publicKey.substring(2);
            String xhex = publicKey.substring(0, 64);
            String yhex = publicKey.substring(64, 128);
            ecPublicKeyParameters = BCUtil.toSm2Params(xhex, yhex);
        } else {
            PublicKey p = BCUtil.decodeECPoint(publicKey, SmUtil.SM2_CURVE_NAME);
            ecPublicKeyParameters = BCUtil.toParams(p);
        }
        //创建sm2 对象
        SM2 sm2 = new SM2(null, ecPublicKeyParameters);

        return sm2.verifyHex(HexUtil.encodeHexStr(content), sign);
    }

    /**
     * 去除接收到的base64密文信息倍数补充符号(方便后续解密处理)
     * Java中对SM2加密后的密文进行Base64编码，并将其作为字符串发送到其他接口时，如果密文的字节数不是3的倍数，就可能会出现结尾多余一个或两个等号的情况
     * 在处理Base64编码的数据时，通常需要将末尾的填充符号（如果有的话）去掉，然后将数据解码回原始格式。因此，接收到的数据末尾的=符号可以被视为Base64编码的填充符号，应该在解码前被移除。
     *
     * @param data base64密文信息
     * @return 结果集
     */
    public static String replaceBase64Symbol(String data){
        if (StrUtil.isNotBlank(data) && data.endsWith(STRING_EQUAL) ){
            //正则匹配去除后缀=符号
            String regex = "=+$";
            data = data.replaceAll(regex, "");
        }
        return data;
    }


    public static void main(String[] args) {
        SmKeyPair smKeyPair = genKeyPair();
        String priKey = smKeyPair.getPriKey();
        System.out.println("私钥:"+ priKey);
        String pubKey = smKeyPair.getPubKey();
        System.out.println("公钥:"+ pubKey);
        //明文
        String text = "123123123";
        System.out.println("测试明文文本:" + text);
        //签名测试
        String sign = sign(priKey, text);
        System.out.println("生成签名:" + sign);
        //验签测试
        boolean verify = verify(pubKey, text, sign);
        System.out.println("验签结果" + verify);

        //加解密测试
        String encryptData = encrypt(pubKey, text);
        System.out.println("加密结果:" + encryptData);
        String decryptData = decrypt(priKey, encryptData);
        System.out.println("解密结果:" + decryptData);

    }
}

