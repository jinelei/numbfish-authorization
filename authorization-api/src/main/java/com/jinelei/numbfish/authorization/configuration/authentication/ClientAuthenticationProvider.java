package com.jinelei.numbfish.authorization.configuration.authentication;

import java.util.Map;

import com.jinelei.numbfish.common.helper.SignatureHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.jinelei.numbfish.authorization.property.AuthorizationProperty;

@SuppressWarnings("unused")
public class ClientAuthenticationProvider implements AuthenticationProvider {
    private static final Logger log = LoggerFactory.getLogger(ClientAuthenticationProvider.class);

    private final SignatureHelper signatureHelper;

    public ClientAuthenticationProvider(AuthorizationProperty property) {
        this.signatureHelper = new SignatureHelper(property.getSignatureHeader(), property.getTimestampHeader(), property.getAccessKeyHeader());
    }

    private void checkSignature(String accessKey, String secretKey, String timestamp, String signature, Map<String, String> params) {
        String generateSignature;
        try {
            generateSignature = signatureHelper.generateSignature(accessKey, secretKey, timestamp, params);
        } catch (RuntimeException e) {
            log.error("签名失败：{}", e.getMessage());
            throw new BadCredentialsException("签名失败");
        }
        // 比较签名是否一致
        if (!signature.equalsIgnoreCase(generateSignature)) {
            throw new BadCredentialsException("签名不匹配");
        }
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ClientAuthenticationToken token = (ClientAuthenticationToken) authentication;
        checkSignature(token.getAccessKey(), token.getSecretKey(), token.getTimestamp(), token.getSignature(), token.getParams());
        token.setAuthenticated(true);
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ClientAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
