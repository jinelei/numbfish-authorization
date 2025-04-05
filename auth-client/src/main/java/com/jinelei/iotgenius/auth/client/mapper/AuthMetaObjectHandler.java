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
    public static final String CREATED_USER_ID = "createdUserId";
    public static final String CREATED_TIME = "createdTime";
    public static final String UPDATED_USER_ID = "updatedUserId";
    public static final String UPDATED_TIME = "updatedTime";

    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            Optional.ofNullable(AuthorizationHelper.currentUser())
                    .map(UserResponse::getId)
                    .ifPresent(id -> this.strictInsertFill(metaObject, CREATED_USER_ID, Long.class, id));
        } catch (Exception e) {
            log.error("获取当前登录用户信息失败: {}", e.getMessage());
        }
        this.strictInsertFill(metaObject, CREATED_TIME, LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            Optional.ofNullable(AuthorizationHelper.currentUser())
                    .map(UserResponse::getId)
                    .ifPresent(id -> this.strictInsertFill(metaObject, UPDATED_USER_ID, Long.class, id));
        } catch (Exception e) {
            log.error("获取当前登录用户信息失败: {}", e.getMessage());
        }
        this.strictUpdateFill(metaObject, UPDATED_TIME, LocalDateTime.class, LocalDateTime.now());
    }

}