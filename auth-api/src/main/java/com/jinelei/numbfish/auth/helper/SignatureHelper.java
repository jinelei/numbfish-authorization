package com.jinelei.numbfish.auth.helper;

import com.jinelei.numbfish.common.exception.InternalException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 签名工具类
 */
@SuppressWarnings("unused")
public class SignatureHelper {

    /**
     * 生成签名
     *
     * @param sortedParams 参数列表，按照key升序排序
     * @param secretKey    密钥
     * @return 签名
     */
    public static String generateSignature(String sortedParams, String secretKey) {
        // 拼接参数和密钥
        final String data = sortedParams + "&" + secretKey;
        // 使用HMAC-SHA256算法生成签名
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] hash = sha256_HMAC.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new InternalException("生成签名失败: " + e.getMessage());
        }
    }
}
