-- 初始化系统管理员
INSERT INTO user (id, username, password, avatar, email, phone, remark, created_user_id, created_time, updated_user_id, updated_time) VALUES (0, '{USERNAME}', '{PASSWORD}', '{AVATAR}', '{EMAIL}', '{PHONE}', '{REMARK}', 0, NOW(), 0, NOW());

