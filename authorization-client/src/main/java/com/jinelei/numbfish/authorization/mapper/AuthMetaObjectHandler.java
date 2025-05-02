package com.jinelei.numbfish.authorization.mapper;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.jinelei.numbfish.authorization.configuration.authentication.ClientAuthenticationToken;
import com.jinelei.numbfish.authorization.configuration.authentication.TokenAuthenticationToken;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@SuppressWarnings("unused")
@Component
public class AuthMetaObjectHandler implements MetaObjectHandler {
    private static final Logger log = LoggerFactory.getLogger(AuthMetaObjectHandler.class);
    public static final String CREATED_USER_ID = "createdUserId";
    public static final String CREATED_TIME = "createdTime";
    public static final String UPDATED_USER_ID = "updatedUserId";
    public static final String UPDATED_TIME = "updatedTime";

    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            Optional.ofNullable(SecurityContextHolder.getContext())
                    .map(SecurityContext::getAuthentication)
                    .ifPresent(a -> {
                        if (a instanceof ClientAuthenticationToken token) {
                            this.strictInsertFill(metaObject, CREATED_USER_ID, Long.class, token.getClientId());
                        }
                        if (a instanceof TokenAuthenticationToken token) {
                            this.strictInsertFill(metaObject, CREATED_USER_ID, Long.class, token.getUserId());
                        }
                    });
        } catch (Exception e) {
            log.error("获取当前登录用户失败: {}", e.getMessage());
        }
        this.strictInsertFill(metaObject, CREATED_TIME, LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            Optional.ofNullable(SecurityContextHolder.getContext())
                    .map(SecurityContext::getAuthentication)
                    .ifPresent(a -> {
                        if (a instanceof ClientAuthenticationToken token) {
                            this.strictInsertFill(metaObject, CREATED_USER_ID, Long.class, token.getClientId());
                        }
                        if (a instanceof TokenAuthenticationToken token) {
                            this.strictInsertFill(metaObject, CREATED_USER_ID, Long.class, token.getUserId());
                        }
                    });
        } catch (Exception e) {
            log.error("获取当前登录用户失败: {}", e.getMessage());
        }
        this.strictUpdateFill(metaObject, UPDATED_TIME, LocalDateTime.class, LocalDateTime.now());
    }

}