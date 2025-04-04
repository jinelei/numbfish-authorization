package com.jinelei.iotgenius.auth.client.mapper;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.jinelei.iotgenius.auth.dto.user.UserResponse;
import com.jinelei.iotgenius.auth.helper.AuthorizationHelper;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@SuppressWarnings("unused")
@Component
public class AuthMetaObjectHandler implements MetaObjectHandler {
    private static final Logger log = LoggerFactory.getLogger(AuthMetaObjectHandler.class);

    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            Optional.ofNullable(AuthorizationHelper.currentUser())
                    .map(UserResponse::getId)
                    .ifPresent(id -> this.strictInsertFill(metaObject, "createdUserId", Long.class, id));
        } catch (Exception e) {
            log.error("获取当前用户信息失败", e);
        }
        this.strictInsertFill(metaObject, "createdTime", LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            Optional.ofNullable(AuthorizationHelper.currentUser())
                    .map(UserResponse::getId)
                    .ifPresent(id -> this.strictInsertFill(metaObject, "updatedUserId", Long.class, id));
        } catch (Exception e) {
            log.error("获取当前用户信息失败", e);
        }
        this.strictUpdateFill(metaObject, "updatedTime", LocalDateTime.class, LocalDateTime.now());
    }

}