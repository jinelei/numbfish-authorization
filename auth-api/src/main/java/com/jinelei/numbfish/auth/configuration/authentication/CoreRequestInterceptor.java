package com.jinelei.numbfish.auth.configuration.authentication;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinelei.numbfish.auth.helper.SignatureHelper;
import com.jinelei.numbfish.auth.property.AuthorizationProperty;
import com.jinelei.numbfish.auth.property.ClientProperty;
import com.jinelei.numbfish.common.exception.InternalException;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("unused")
public class CoreRequestInterceptor implements RequestInterceptor {
    private static final Logger log = LoggerFactory.getLogger(CoreRequestInterceptor.class);
    private final ObjectMapper objectMapper;
    private final SignatureHelper signatureHelper;
    private final AuthorizationProperty property;

    public CoreRequestInterceptor(ObjectMapper objectMapper, AuthorizationProperty property) {
        this.objectMapper = objectMapper;
        this.property = property;
        this.signatureHelper = new SignatureHelper(property);
    }


    @Override
    public void apply(RequestTemplate template) {
        final HttpMethod method = Optional.of(template)
                .map(RequestTemplate::method)
                .map(HttpMethod::valueOf)
                .orElse(null);
        final MediaType contentType = Optional.of(template)
                .map(RequestTemplate::headers)
                .map(i -> i.get("Content-Type"))
                .filter(i -> !i.isEmpty())
                .flatMap(i -> i.stream().findFirst())
                .map(MediaType::valueOf)
                .orElse(null);
        final Map<String, String> params = new HashMap<>();
        if (HttpMethod.POST.equals(method) && MediaType.APPLICATION_JSON.equals(contentType)) {
            Optional.ofNullable(template.body())
                    .filter(c -> c.length != 0).ifPresent(bytes -> {
                        try {
                            params.putAll(objectMapper.readValue(bytes, new TypeReference<Map<String, String>>() {
                            }));
                        } catch (IOException e) {
                            log.error("登陆失败: body参数不合法");
                        }
                    });
        }
        // 当请求方法是GET时，检查是否具有query参数
        if (HttpMethod.GET.equals(method)) {
            Optional.of(template)
                    .map(RequestTemplate::queries)
                    .map(Map::entrySet)
                    .stream().flatMap(Set::stream)
                    .forEach(entry -> {
                        if (!entry.getValue().isEmpty()) {
                            params.put(entry.getKey(), entry.getValue().stream().findFirst().get());
                        }
                    });
        }
        final String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        final String accessKey = Optional.ofNullable(property)
                .map(AuthorizationProperty::getClient)
                .map(ClientProperty::getAccessKey)
                .orElseThrow(() -> new InternalException("accessKey为空"));
        final String secretKey = Optional.of(property)
                .map(AuthorizationProperty::getClient)
                .map(ClientProperty::getSecretKey)
                .orElseThrow(() -> new InternalException("secretKey为空"));
        final String signature = signatureHelper.generateSignature(accessKey, secretKey, timestamp, params);
        Optional.of(property)
                .map(AuthorizationProperty::getAccessKeyHeader)
                .ifPresent(header -> template.header(header, accessKey));
        Optional.of(property)
                .map(AuthorizationProperty::getTimestampHeader)
                .ifPresent(header -> template.header(header, timestamp));
        Optional.of(property)
                .map(AuthorizationProperty::getSignatureHeader)
                .ifPresent(header -> template.header(header, signature));
    }
}
