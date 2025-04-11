package com.jinelei.iotgenius.auth.client.configuration.authentication;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.jinelei.iotgenius.auth.property.AuthorizationProperty;

@SuppressWarnings("unused")
public class ClientAuthenticationProvider implements AuthenticationProvider {

    private final AuthorizationProperty property;

    public ClientAuthenticationProvider(AuthorizationProperty property) {
        this.property = property;
    }

    private void checkSignature(String accessKey, String secretKey, String signature, Map<String, String> params) {
        Optional.ofNullable(accessKey).orElseThrow(() -> new BadCredentialsException("AccessKey不存在"));
        Optional.ofNullable(secretKey).orElseThrow(() -> new BadCredentialsException("SecretKey不存在"));
        Optional.ofNullable(signature).orElseThrow(() -> new BadCredentialsException("Signature不存在"));
        Optional.ofNullable(params).orElseThrow(() -> new BadCredentialsException("参数不存在"));
        // 对参数进行排序并拼接
        final String string = params.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .filter(i -> !i.getKey().equals(property.getSignatureHeader()))
                .filter(i -> !i.getKey().equals(property.getTimestampHeader()))
                .filter(i -> !i.getKey().equals(property.getAccessKeyHeader()))
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));
        // 对参数进行签名，使用HMAC-SHA256算法
        String sign = "";
        try {
            final Mac mac = Mac.getInstance("HmacSHA256");
            final Key key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            mac.init(key);
            byte[] bytes = mac.doFinal(String.format("%s&AccessKey=%s", sign, accessKey).getBytes());
            HexFormat hexFormat = HexFormat.of().withUpperCase();
            sign = hexFormat.formatHex(bytes);
        } catch (IllegalStateException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new BadCredentialsException("签名失败");
        }
        // 比较签名是否一致
        if (!sign.equals(signature)) {
            throw new BadCredentialsException("签名不匹配");
        }
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ClientAuthenticationToken token = (ClientAuthenticationToken) authentication;
        checkSignature(token.getAccessKey(), token.getSecretKey(), token.getSignature(), token.getParams());
        token.setAuthenticated(true);
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ClientAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
