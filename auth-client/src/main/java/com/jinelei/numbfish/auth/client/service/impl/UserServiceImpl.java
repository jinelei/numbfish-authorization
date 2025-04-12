package com.jinelei.numbfish.auth.client.service.impl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.jinelei.numbfish.auth.property.AdminProperty;
import com.jinelei.numbfish.auth.property.AuthorizationProperty;
import org.apache.ibatis.executor.BatchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinelei.numbfish.auth.client.configuration.authentication.permission.instance.PermissionInstance;
import com.jinelei.numbfish.auth.client.convertor.PermissionConvertor;
import com.jinelei.numbfish.auth.client.convertor.RoleConvertor;
import com.jinelei.numbfish.auth.client.convertor.UserConvertor;
import com.jinelei.numbfish.auth.client.domain.PermissionEntity;
import com.jinelei.numbfish.auth.client.domain.RoleEntity;
import com.jinelei.numbfish.auth.client.domain.RolePermissionEntity;
import com.jinelei.numbfish.auth.client.domain.UserEntity;
import com.jinelei.numbfish.auth.client.domain.UserRoleEntity;
import com.jinelei.numbfish.auth.client.mapper.UserMapper;
import com.jinelei.numbfish.auth.client.service.PermissionService;
import com.jinelei.numbfish.auth.client.service.RolePermissionService;
import com.jinelei.numbfish.auth.client.service.RoleService;
import com.jinelei.numbfish.auth.client.service.UserRoleService;
import com.jinelei.numbfish.auth.client.service.UserService;
import com.jinelei.numbfish.auth.dto.user.UserCreateRequest;
import com.jinelei.numbfish.auth.dto.user.UserDeleteRequest;
import com.jinelei.numbfish.auth.dto.user.UserLoginRequest;
import com.jinelei.numbfish.auth.dto.user.UserQueryRequest;
import com.jinelei.numbfish.auth.dto.user.UserResponse;
import com.jinelei.numbfish.auth.dto.user.UserUpdatePasswordRequest;
import com.jinelei.numbfish.auth.dto.user.UserUpdateRequest;
import com.jinelei.numbfish.auth.helper.AuthorizationHelper;
import com.jinelei.numbfish.common.exception.InvalidArgsException;

@SuppressWarnings("unused")
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity>
        implements UserService, UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final AuthorizationProperty property;

    @Autowired
    protected UserConvertor userConvertor;
    @Autowired
    protected RoleConvertor roleConvertor;
    @Autowired
    protected PermissionConvertor permissionConvertor;
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
    protected StringRedisTemplate redisTemplate;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected AuthorizationHelper authorizationHelper;

    public UserServiceImpl(AuthorizationProperty property) {
        this.property = property;
    }

    @Override
    public void create(UserCreateRequest request) {
        AuthorizationHelper.hasPermissions(PermissionInstance.USER_CREATE);
        final UserEntity entity = userConvertor.entityFromCreateRequest(request);
        Optional.ofNullable(entity).orElseThrow(() -> new InvalidArgsException("角色信息不合法"));
        Optional.of(entity).map(UserEntity::getPassword).ifPresentOrElse(password -> {
            if (password.length() < 6) {
                throw new InvalidArgsException("密码长度不能小于6位");
            }
            entity.setPassword(passwordEncoder.encode(password));
        }, () -> entity.setPassword(passwordEncoder.encode(PASSWORD)));
        int inserted = baseMapper.insert(entity);
        log.debug("创建用户: {}", inserted);
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
                    log.debug("创建用户角色关系: {}", insert.size());
                });
        Assert.state(inserted == 1, "角色创建失败");
    }

    @Override
    public void delete(UserDeleteRequest request) {
        AuthorizationHelper.hasPermissions(PermissionInstance.USER_DELETE);
        if (Objects.nonNull(request.getId())) {
            int deleted = baseMapper.deleteById(request.getId());
            log.debug("删除用户: {}", deleted);
            Assert.state(deleted == 1, "角色删除失败");
            LambdaUpdateWrapper<UserRoleEntity> wrapper = Wrappers.lambdaUpdate(UserRoleEntity.class)
                    .eq(UserRoleEntity::getUserId, request.getId());
            int deletedUserRole = userRoleService.getBaseMapper().delete(wrapper);
            log.debug("删除用户角色关系: {}", deletedUserRole);
        } else if (!CollectionUtils.isEmpty(request.getIds())) {
            int deleted = baseMapper.deleteByIds(request.getIds());
            log.debug("删除用户: {}", deleted);
            Assert.state(deleted == request.getIds().size(), "角色删除失败");
            int deletedUserRole = userRoleService.getBaseMapper().delete(
                    Wrappers.lambdaUpdate(UserRoleEntity.class).in(UserRoleEntity::getUserId, request.getIds()));
            log.debug("删除用户角色关系: {}", deletedUserRole);
        } else {
            throw new InvalidArgsException("不支持的删除方式: " + request);
        }
    }

    @Override
    public void update(@Validated UserUpdateRequest request) {
        AuthorizationHelper.hasPermissions(PermissionInstance.USER_UPDATE);
        final String username = Optional.ofNullable(request).map(UserUpdateRequest::getUsername)
                .orElseThrow(() -> new InvalidArgsException("用户名不能为空"));
        LambdaUpdateWrapper<UserEntity> wrapper = Wrappers.lambdaUpdate(UserEntity.class);
        wrapper.eq(UserEntity::getId, request.getId());
        wrapper.set(UserEntity::getUsername, request.getUsername());
        wrapper.set(UserEntity::getAvatar, request.getAvatar());
        wrapper.set(UserEntity::getEmail, request.getEmail());
        wrapper.set(UserEntity::getPhone, request.getPhone());
        wrapper.set(UserEntity::getRemark, request.getRemark());
        int updated = baseMapper.update(wrapper);
        log.debug("更新用户: {}", updated);
        int deleted = userRoleService.getBaseMapper()
                .delete(Wrappers.lambdaUpdate(UserRoleEntity.class).eq(UserRoleEntity::getUserId, request.getId()));
        log.debug("删除用户角色关系: {}", deleted);
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
                    log.debug("创建用户角色关系: {}", insert.size());
                });
        Assert.state(updated == 1, "角色更新失败");
    }

    @Override
    public UserEntity get(UserQueryRequest request) {
        AuthorizationHelper.hasPermissions(PermissionInstance.USER_DETAIL);
        LambdaQueryWrapper<UserEntity> wrapper = Wrappers.lambdaQuery(UserEntity.class);
        wrapper.eq(Objects.nonNull(request.getId()), UserEntity::getId, request.getId());
        wrapper.like(Objects.nonNull(request.getUsername()), UserEntity::getUsername, request.getUsername());
        wrapper.like(Objects.nonNull(request.getEmail()), UserEntity::getEmail, request.getEmail());
        wrapper.like(Objects.nonNull(request.getPhone()), UserEntity::getPhone, request.getPhone());
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public List<UserEntity> list(UserQueryRequest request) {
        AuthorizationHelper.hasPermissions(PermissionInstance.USER_SUMMARY);
        LambdaQueryWrapper<UserEntity> wrapper = Wrappers.lambdaQuery(UserEntity.class);
        wrapper.eq(Objects.nonNull(request.getId()), UserEntity::getId, request.getId());
        wrapper.like(Objects.nonNull(request.getUsername()), UserEntity::getUsername, request.getUsername());
        wrapper.like(Objects.nonNull(request.getEmail()), UserEntity::getEmail, request.getEmail());
        wrapper.like(Objects.nonNull(request.getPhone()), UserEntity::getPhone, request.getPhone());
        return baseMapper.selectList(wrapper);
    }

    @Override
    public IPage<UserEntity> page(IPage<UserEntity> page, UserQueryRequest request) {
        AuthorizationHelper.hasPermissions(PermissionInstance.USER_SUMMARY);
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
        final String token = UUID.randomUUID().toString();
        final String keyUserTokenInfo = GENERATE_TOKEN_INFO.apply(token);
        redisTemplate.opsForValue().set(keyUserTokenInfo, userEntity.getUsername());
        stringRedisTemplate.expire(keyUserTokenInfo, Duration.ofMinutes(30));
        log.debug("用户登录: {}", userEntity);
        return token;
    }

    @Override
    public void logout() {
        String token = "";
        stringRedisTemplate.delete(GENERATE_TOKEN_INFO.apply(token));
        log.debug("用户登出");
    }

    @Override
    public void updatePassword(UserUpdatePasswordRequest request) {
        AuthorizationHelper.hasPermissions(PermissionInstance.USER_UPDATE);
        final String username = Optional.ofNullable(request).map(UserUpdatePasswordRequest::getUsername)
                .orElseThrow(() -> new InvalidArgsException("用户名不能为空"));
        String password = Optional.of(request).map(UserUpdatePasswordRequest::getPassword).orElse(PASSWORD);
        if (password.length() < 6) {
            throw new InvalidArgsException("密码长度不能小于6位");
        }
        LambdaUpdateWrapper<UserEntity> wrapper = Wrappers.lambdaUpdate(UserEntity.class);
        wrapper.eq(UserEntity::getUsername, request.getUsername());
        wrapper.set(UserEntity::getPassword, passwordEncoder.encode(password));
        int updated = baseMapper.update(wrapper);
        log.debug("更新用户密码: {}", updated);
        Assert.state(updated == 1, "用户密码更新失败");
    }

    @Override
    public UserResponse convert(UserEntity entity) {
        return userConvertor.entityToResponse(entity);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<UserEntity> wrapper = Wrappers.lambdaQuery(UserEntity.class);
        wrapper.eq(UserEntity::getUsername, username);
        UserEntity userEntity = baseMapper.selectOne(wrapper);
        Optional.ofNullable(userEntity).orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
        final Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (Optional.ofNullable(property).map(AuthorizationProperty::getAdmin).map(AdminProperty::getUsername).map(username::equals).orElse(false)) {
            final List<RoleEntity> roleEntities = roleService.list(Wrappers.lambdaQuery(RoleEntity.class));
            if (!roleEntities.isEmpty()) {
                roleEntities.parallelStream().map(RoleEntity::getCode).map(i -> "ROLE_" + i).map(SimpleGrantedAuthority::new).forEach(authorities::add);
            }
            final List<PermissionEntity> permissionEntities = permissionService.list(Wrappers.lambdaQuery(PermissionEntity.class));
            if (!permissionEntities.isEmpty()) {
                permissionEntities.parallelStream().map(PermissionEntity::getCode).map(SimpleGrantedAuthority::new).forEach(authorities::add);
            }
        } else {
            // 查询用户关联的所有的角色
            final List<UserRoleEntity> userRoleEntities = userRoleService.list(Wrappers.lambdaQuery(UserRoleEntity.class).eq(UserRoleEntity::getUserId, userEntity.getId()));
            final List<Long> roleIds = userRoleEntities.parallelStream().map(UserRoleEntity::getRoleId).toList();
            if (!roleIds.isEmpty()) {
                final List<RoleEntity> roleEntities = roleService.list(Wrappers.lambdaQuery(RoleEntity.class).in(RoleEntity::getId, roleIds));
                roleEntities.parallelStream().map(RoleEntity::getCode).map(i -> "ROLE_" + i).map(SimpleGrantedAuthority::new).forEach(authorities::add);
                final List<RolePermissionEntity> rolePermissionEntities = rolePermissionService.list(Wrappers.lambdaQuery(RolePermissionEntity.class).in(RolePermissionEntity::getRoleId, roleIds));
                final List<Long> permissionIds = rolePermissionEntities.parallelStream().map(RolePermissionEntity::getPermissionId).toList();
                if (!permissionIds.isEmpty()) {
                    final List<PermissionEntity> permissionEntities = permissionService.list(Wrappers.lambdaQuery(PermissionEntity.class).in(PermissionEntity::getId, permissionIds));
                    permissionEntities.parallelStream().map(PermissionEntity::getCode).map(SimpleGrantedAuthority::new).forEach(authorities::add);
                }
            }
        }
        return new User(userEntity.getUsername(), userEntity.getPassword(), authorities);
    }

}
