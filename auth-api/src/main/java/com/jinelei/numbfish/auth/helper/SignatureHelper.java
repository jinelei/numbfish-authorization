package com.jinelei.numbfish.auth.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jinelei.numbfish.auth.property.AuthorizationProperty;
import com.jinelei.numbfish.common.exception.InternalException;
import com.jinelei.numbfish.common.wrapper.RepeatRequestWrapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 签名工具类
 */
@SuppressWarnings("unused")
public class SignatureHelper {
    private final AuthorizationProperty property;

    public SignatureHelper(AuthorizationProperty property) {
        this.property = property;
    }

    public String generateSignature(String accessKey, String secretKey, String timestamp, Map<String, String> params) {
        Optional.ofNullable(property).orElseThrow(() -> new InternalException("AuthorizationProperty不存在"));
        Optional.ofNullable(accessKey).orElseThrow(() -> new BadCredentialsException("AccessKey不存在"));
        Optional.ofNullable(secretKey).orElseThrow(() -> new BadCredentialsException("SecretKey不存在"));
        Optional.ofNullable(timestamp).orElseThrow(() -> new BadCredentialsException("Timestamp不存在"));
        Optional.ofNullable(params).orElseThrow(() -> new BadCredentialsException("参数不存在"));
        // 对参数进行排序并拼接
        final StringBuilder buffer = new StringBuilder();
        final String string = params.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .filter(i -> !i.getKey().equals(property.getSignatureHeader()))
                .filter(i -> !i.getKey().equals(property.getTimestampHeader()))
                .filter(i -> !i.getKey().equals(property.getAccessKeyHeader()))
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));
        // 追加AccessKey、Timestamp和SecretKey
        buffer.append(string);
        if (!string.isEmpty()) {
            buffer.append("&");
        }
        buffer.append("AccessKey=").append(accessKey);
        buffer.append("&Timestamp=").append(timestamp);
        // 对参数进行签名，使用HMAC-SHA256算法
        String sign;
        try {
            final Mac mac = Mac.getInstance("HmacSHA256");
            final Key key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(key);
            byte[] bytes = mac.doFinal(buffer.toString().getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(bytes);
        } catch (IllegalStateException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new BadCredentialsException("签名失败");
        }
    }

}
