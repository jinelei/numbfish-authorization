package com.jinelei.iotgenius.auth.authentication;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@SuppressWarnings("unused")
public class ClientAuthenticationProvider implements AuthenticationProvider {

    private void checkSignature(String accessKey, String secretKey, String signature, Map<String, String> params) {
        Optional.ofNullable(accessKey).orElseThrow(() -> new BadCredentialsException("AccessKey不存在"));
        Optional.ofNullable(secretKey).orElseThrow(() -> new BadCredentialsException("SecretKey不存在"));
        Optional.ofNullable(signature).orElseThrow(() -> new BadCredentialsException("Signature不存在"));
        Optional.ofNullable(params).orElseThrow(() -> new BadCredentialsException("参数不存在"));
        // 对参数进行排序并拼接
        params.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .filter(i -> !i.getKey().equals("signature"))
                .filter(i -> !i.getKey().equals("accessKey"))
                .filter(i -> !i.getKey().equals("secretKey"))
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));
        // 对参数进行签名，使用HMAC-SHA256算法
        String sign = "";
        try {
            final Mac mac = Mac.getInstance("HmacSHA256");
            final Key key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            mac.init(key);
            sign = mac.doFinal(params.toString().getBytes()).toString();
        } catch (Exception e) {
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
