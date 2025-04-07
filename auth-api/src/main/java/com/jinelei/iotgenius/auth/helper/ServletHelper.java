package com.jinelei.iotgenius.auth.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinelei.iotgenius.common.utils.ThrowableStackTraceUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public class ServletHelper {
    private static final Logger log = LoggerFactory.getLogger(ServletHelper.class);
    private static final List<String> SUPPORTED_CHARSETS = Arrays.asList("UTF-8", "ISO-8859-1");

    private final ObjectMapper objectMapper;

    public ServletHelper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void response(HttpServletRequest request, HttpServletResponse response, Object object) {
        response(request, response, MediaType.APPLICATION_JSON, HttpStatus.OK, object);
    }

    public void response(HttpServletRequest request, HttpServletResponse response, HttpStatus httpStatus, Object object) {
        response(request, response, MediaType.APPLICATION_JSON, httpStatus, object);
    }

    public void response(HttpServletRequest request, HttpServletResponse response, MediaType mediaType, HttpStatus httpStatus, Object object) {
        final String acceptCharsetHeader = request.getHeader(HttpHeaders.ACCEPT_CHARSET);
        Optional.ofNullable(acceptCharsetHeader)
                .map(s -> s.split(","))
                .map(arr -> Arrays.stream(arr)
                        .map(String::trim)
                        .map(s -> s.split(";")[0])
                        .filter(SUPPORTED_CHARSETS::contains)
                        .findFirst())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .ifPresentOrElse(response::setCharacterEncoding,
                        () -> response.setCharacterEncoding(StandardCharsets.UTF_8.name()));
        response.setContentType(mediaType.toString());
        try {
            response.getWriter().write(objectMapper.writeValueAsString(object));
        } catch (Throwable throwable) {
            log.error("响应序列化失败：{}", ThrowableStackTraceUtils.getStackTraceAsString(throwable));
        }
        response.setStatus(httpStatus.value());
    }

}
