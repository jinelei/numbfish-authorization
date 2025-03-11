package com.jinelei.iotgenius.auth.client.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinelei.iotgenius.auth.client.convertor.UserConvertor;
import com.jinelei.iotgenius.auth.client.domain.*;
import com.jinelei.iotgenius.auth.client.mapper.UserMapper;
import com.jinelei.iotgenius.auth.client.service.*;
import com.jinelei.iotgenius.auth.dto.user.*;
import com.jinelei.iotgenius.common.exception.InvalidArgsException;
import org.apache.ibatis.executor.BatchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("unused")
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity>
        implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    protected UserConvertor userConvertor;
    @Autowired
    protected UserRoleService userRoleService;
    @Autowired
    protected PasswordEncoder passwordEncoder;
    @Autowired
    protected RoleService roleService;
    @Autowired
    protected RolePermissionService rolePermissionService;
    @Autowired
    protected PermissionService permissionService;
    @Autowired
    protected StringRedisTemplate stringRedisTemplate;
    @Autowired
    protected RedisTemplate<String, UserResponse> userRedisTemplate;
    @Autowired
    protected ObjectMapper objectMapper;

    @Override
    public void create(UserCreateRequest request) {
        final UserEntity entity = userConvertor.entityFromCreateRequest(request);
        Optional.ofNullable(entity).orElseThrow(() -> new InvalidArgsException("角色信息不合法"));
        Optional.ofNullable(entity).map(UserEntity::getPassword).ifPresentOrElse(password -> {
            if (password.length() < 6) {
                throw new InvalidArgsException("密码长度不能小于6位");
            }
            entity.setPassword(passwordEncoder.encode(password));
        }, () -> {
            entity.setPassword(passwordEncoder.encode(PASSWORD));
        });
        int inserted = baseMapper.insert(entity);
        log.info("创建用户: {}", inserted);
        Optional.ofNullable(request.getRoleIds())
                .filter(i -> !CollectionUtils.isEmpty(i))
                .ifPresent(list -> {
                    final List<UserRoleEntity> userRoleEntities = list.parallelStream()
                            .map(id -> {
                                UserRoleEntity userRole = new UserRoleEntity();
                                userRole.setUserId(entity.getId());
                                userRole.setRoleId(id);
                                return userRole;
                            }).toList();
                    List<BatchResult> insert = userRoleService.getBaseMapper().insert(userRoleEntities);
                    log.info("创建用户角色关系: {}", insert.size());
                });
        Assert.state(inserted == 1, "角色创建失败");
    }

    @Override
    public void delete(UserDeleteRequest request) {
        if (Objects.nonNull(request.getId())) {
            int deleted = baseMapper.deleteById(request.getId());
            log.info("删除用户: {}", deleted);
            Assert.state(deleted == 1, "角色删除失败");
            LambdaUpdateWrapper<UserRoleEntity> wrapper = Wrappers.lambdaUpdate(UserRoleEntity.class).eq(UserRoleEntity::getUserId, request.getId());
            int deletedUserRole = userRoleService.getBaseMapper().delete(wrapper);
            log.info("删除用户角色关系: {}", deletedUserRole);
        } else if (!CollectionUtils.isEmpty(request.getIds())) {
            int deleted = baseMapper.deleteByIds(request.getIds());
            log.info("删除用户: {}", deleted);
            Assert.state(deleted == request.getIds().size(), "角色删除失败");
            int deletedUserRole = userRoleService.getBaseMapper().delete(Wrappers.lambdaUpdate(UserRoleEntity.class).in(UserRoleEntity::getUserId, request.getIds()));
            log.info("删除用户角色关系: {}", deletedUserRole);
        } else {
            throw new InvalidArgsException("不支持的删除方式: " + request);
        }
    }

    @Override
    public void update(@Validated UserUpdateRequest request) {
        LambdaUpdateWrapper<UserEntity> wrapper = Wrappers.lambdaUpdate(UserEntity.class);
        wrapper.eq(UserEntity::getId, request.getId());
        wrapper.set(UserEntity::getUsername, request.getUsername());
        wrapper.set(UserEntity::getPassword, request.getPassword());
        wrapper.set(UserEntity::getAvatar, request.getAvatar());
        wrapper.set(UserEntity::getEmail, request.getEmail());
        wrapper.set(UserEntity::getPhone, request.getPhone());
        wrapper.set(UserEntity::getRemark, request.getRemark());
        int updated = baseMapper.update(wrapper);
        log.info("更新用户: {}", updated);
        int deleted = userRoleService.getBaseMapper().delete(Wrappers.lambdaUpdate(UserRoleEntity.class).eq(UserRoleEntity::getUserId, request.getId()));
        log.info("删除用户角色关系: {}", deleted);
        Optional.ofNullable(request.getRoleIds())
                .filter(i -> !CollectionUtils.isEmpty(i))
                .ifPresent(list -> {
                    final List<UserRoleEntity> userRoleEntities = list.parallelStream()
                            .map(id -> {
                                UserRoleEntity userRole = new UserRoleEntity();
                                userRole.setUserId(request.getId());
                                userRole.setRoleId(id);
                                return userRole;
                            }).toList();
                    List<BatchResult> insert = userRoleService.getBaseMapper().insert(userRoleEntities);
                    log.info("创建用户角色关系: {}", insert.size());
                });
        Assert.state(updated == 1, "角色更新失败");
    }

    @Override
    public UserEntity get(UserQueryRequest request) {
        LambdaQueryWrapper<UserEntity> wrapper = Wrappers.lambdaQuery(UserEntity.class);
        wrapper.eq(Objects.nonNull(request.getId()), UserEntity::getId, request.getId());
        wrapper.like(Objects.nonNull(request.getUsername()), UserEntity::getUsername, request.getUsername());
        wrapper.like(Objects.nonNull(request.getEmail()), UserEntity::getEmail, request.getEmail());
        wrapper.like(Objects.nonNull(request.getPhone()), UserEntity::getPhone, request.getPhone());
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public List<UserEntity> list(UserQueryRequest request) {
        LambdaQueryWrapper<UserEntity> wrapper = Wrappers.lambdaQuery(UserEntity.class);
        wrapper.eq(Objects.nonNull(request.getId()), UserEntity::getId, request.getId());
        wrapper.like(Objects.nonNull(request.getUsername()), UserEntity::getUsername, request.getUsername());
        wrapper.like(Objects.nonNull(request.getEmail()), UserEntity::getEmail, request.getEmail());
        wrapper.like(Objects.nonNull(request.getPhone()), UserEntity::getPhone, request.getPhone());
        return baseMapper.selectList(wrapper);
    }

    @Override
    public IPage<UserEntity> page(IPage<UserEntity> page, UserQueryRequest request) {
        LambdaQueryWrapper<UserEntity> wrapper = Wrappers.lambdaQuery(UserEntity.class);
        wrapper.eq(Objects.nonNull(request.getId()), UserEntity::getId, request.getId());
        wrapper.like(Objects.nonNull(request.getUsername()), UserEntity::getUsername, request.getUsername());
        wrapper.like(Objects.nonNull(request.getEmail()), UserEntity::getEmail, request.getEmail());
        wrapper.like(Objects.nonNull(request.getPhone()), UserEntity::getPhone, request.getPhone());
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public String login(UserLoginRequest request) {
        LambdaQueryWrapper<UserEntity> wrapper = Wrappers.lambdaQuery(UserEntity.class);
        wrapper.eq(UserEntity::getUsername, request.getUsername());
        UserEntity userEntity = baseMapper.selectOne(wrapper);
        if (Objects.isNull(userEntity)) {
            throw new InvalidArgsException("用户不存在");
        }
        if (!passwordEncoder.matches(request.getPassword(), userEntity.getPassword())) {
            throw new InvalidArgsException("密码错误");
        }
        userEntity.setPassword(null);
        userEntity.setRoles(new ArrayList<>());
        userEntity.setPermissions(new ArrayList<>());
        // 查询用户关联的所有的角色
        final List<UserRoleEntity> userRoleEntities = userRoleService.list(Wrappers.lambdaQuery(UserRoleEntity.class).eq(UserRoleEntity::getUserId, userEntity.getId()));
        final List<Long> roleIds = userRoleEntities.parallelStream().map(UserRoleEntity::getRoleId).toList();
        if (!roleIds.isEmpty()) {
            final List<RoleEntity> roleEntities = roleService.list(Wrappers.lambdaQuery(RoleEntity.class).in(RoleEntity::getId, roleIds));
            userEntity.setRoles(roleEntities);
            final List<RolePermissionEntity> rolePermissionEntities = rolePermissionService.list(Wrappers.lambdaQuery(RolePermissionEntity.class).in(RolePermissionEntity::getRoleId, roleIds));
            final List<Long> permissionIds = rolePermissionEntities.parallelStream().map(RolePermissionEntity::getPermissionId).toList();
            if (!permissionIds.isEmpty()) {
                final List<PermissionEntity> permissionEntities = permissionService.list(Wrappers.lambdaQuery(PermissionEntity.class).in(PermissionEntity::getId, permissionIds));
                userEntity.setPermissions(permissionEntities);
            }
        }
        final UserResponse userResponse = userConvertor.entityToResponse(userEntity);
        final String token = UUID.randomUUID().toString();
        final String keyUserTokenInfo = GENERATE_TOKEN_INFO.apply(token);
        userRedisTemplate.opsForValue().set(keyUserTokenInfo, userResponse);
        if (!userEntity.getRoles().isEmpty()) {
            userEntity.getRoles().parallelStream().map(String::valueOf)
                    .map(CACHED_ROLE_ID_TOKEN)
                    .forEach(s -> stringRedisTemplate.opsForSet().add(s, token));
        }
        if (!userEntity.getPermissions().isEmpty()) {
            userEntity.getPermissions().parallelStream().map(String::valueOf)
                    .map(CACHED_PERMISSION_ID_TOKEN)
                    .forEach(s -> stringRedisTemplate.opsForSet().add(s, token));
        }
        stringRedisTemplate.expire(keyUserTokenInfo, Duration.ofMinutes(30));
        log.info("用户登录: {}", userEntity);
        return token;
    }

    @Override
    public void logout() {
        String token = "";
        stringRedisTemplate.delete(GENERATE_TOKEN_INFO.apply(token));
        log.info("用户登出");
    }


    @Override
    public UserResponse convert(UserEntity entity) {
        UserResponse response = userConvertor.entityToResponse(entity);
        return response;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final List<SimpleGrantedAuthority> authorities = new CopyOnWriteArrayList<>();
        final UserEntity userEntity = getBaseMapper().selectOne(Wrappers.lambdaQuery(UserEntity.class).eq(UserEntity::getUsername, username));
        if (Objects.isNull(userEntity)) {
            log.error("用户不存在: {}", username);
            throw new UsernameNotFoundException("用户不存在");
        }
        // 查询用户关联的所有的角色
        final List<UserRoleEntity> userRoleEntities = userRoleService.list(Wrappers.lambdaQuery(UserRoleEntity.class).eq(UserRoleEntity::getUserId, userEntity.getId()));
        final List<Long> roleIds = userRoleEntities.parallelStream().map(UserRoleEntity::getRoleId).toList();
        List<RoleEntity> roleEntities = roleService.list(Wrappers.lambdaQuery(RoleEntity.class).in(RoleEntity::getId, roleIds));
        userEntity.setRoles(roleEntities);
        roleEntities.parallelStream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getCode()))
                .forEach(authorities::add);
        // 查询角色关联的所有的权限(菜单和按钮)
        final List<RolePermissionEntity> rolePermissionEntities = rolePermissionService.list(Wrappers.lambdaQuery(RolePermissionEntity.class).in(RolePermissionEntity::getRoleId, roleIds));
        final List<Long> permissionIds = rolePermissionEntities.parallelStream().map(RolePermissionEntity::getPermissionId).toList();
        // 查询权限关联的所有的菜单和按钮
        final List<PermissionEntity> permissionEntities = permissionService.list(Wrappers.lambdaQuery(PermissionEntity.class).in(PermissionEntity::getId, permissionIds));
        userEntity.setPermissions(permissionEntities);
        permissionEntities.parallelStream()
                .map(permission -> new SimpleGrantedAuthority(permission.getCode()))
                .forEach(authorities::add);
        userEntity.setPassword(null);
        User user = new User(userEntity.getUsername(), userEntity.getPassword(), true, true, true, true, authorities);
        SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
        emptyContext.setAuthentication(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities()));
        SecurityContextHolder.setContext(emptyContext);
        log.info("加载用户: {}", user);
        return user;
    }
}
