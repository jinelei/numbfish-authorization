package com.jinelei.iotgenius.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinelei.iotgenius.view.BaseView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * @Author: jinelei
 * @Description:
 * @Date: 2024/3/17 19:53
 * @Version: 1.0.0
 */
@SuppressWarnings("unused")
@Component
public class ResponseHelper {
    private final static Logger log = LoggerFactory.getLogger(ResponseHelper.class);
    private final ObjectMapper objectMapper;

    public ResponseHelper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 响应
     *
     * @param request  请求
     * @param response 响应
     * @param data     数据
     */
    public void response(HttpServletRequest request, HttpServletResponse response, BaseView<?> data) {
        try {
            response.setHeader("Cache-Control", "no-cache");
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.OK.value());
            String body = objectMapper.writeValueAsString(data);
            response.getWriter().write(body);
            response.getWriter().flush();
        } catch (Throwable throwable) {
            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            throwable.printStackTrace(new PrintWriter(os));
            log.error("response failure: request: {}, response:{}, reason: {}", request, response, os);
        }
    }

    /**
     * 文件卸载
     *
     * @param request     请求
     * @param response    响应
     * @param resource    资源
     * @param contentType 内容类型
     */
    public void download(HttpServletRequest request, HttpServletResponse response, Resource resource,
                         String contentType) {
        try {
            response.setStatus(HttpStatus.OK.value());
            response.setHeader("Cache-Control", "no-cache");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Content-Disposition",
                    String.format("attachment; filename=\"%s\"", resource.getFilename()));
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
            response.setContentLengthLong(resource.contentLength());
            response.setContentType(contentType);
            ReadableByteChannel readableByteChannel = Channels.newChannel(resource.getInputStream());
            WritableByteChannel writableByteChannel = Channels.newChannel(response.getOutputStream());
            ByteBuffer allocate = ByteBuffer.allocate(1024);
            while (readableByteChannel.read(allocate) != -1) {
                allocate.flip();
                writableByteChannel.write(allocate);
                allocate.clear();
            }
            response.getWriter().flush();
        } catch (Throwable ignored) {
        }
    }

    /**
     * 文件卸载
     *
     * @param request  请求
     * @param response 响应
     * @param resource 资源
     */
    public void download(HttpServletRequest request, HttpServletResponse response, Resource resource) {
        download(request, response, resource, MediaType.APPLICATION_OCTET_STREAM_VALUE);
    }
}
