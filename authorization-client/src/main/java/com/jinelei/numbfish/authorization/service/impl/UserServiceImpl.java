package com.jinelei.numbfish.authorization.service.impl;

import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.jinelei.numbfish.authorization.configuration.authentication.TokenAuthenticationToken;
import com.jinelei.numbfish.authorization.configuration.authentication.TokenCacheKeyGenerator;
import com.jinelei.numbfish.authorization.dto.*;
import com.jinelei.numbfish.authorization.enumeration.TreeBuildMode;
import com.jinelei.numbfish.authorization.mapper.PermissionMapper;
import com.jinelei.numbfish.authorization.mapper.RoleMapper;
import com.jinelei.numbfish.authorization.property.AdminProperty;
import com.jinelei.numbfish.authorization.property.AuthorizationProperty;
import com.jinelei.numbfish.common.entity.BaseEntity;
import org.apache.ibatis.executor.BatchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.jinelei.numbfish.authorization.convertor.PermissionConvertor;
import com.jinelei.numbfish.authorization.convertor.RoleConvertor;
import com.jinelei.numbfish.authorization.convertor.UserConvertor;
import com.jinelei.numbfish.authorization.entity.PermissionEntity;
import com.jinelei.numbfish.authorization.entity.RoleEntity;
import com.jinelei.numbfish.authorization.entity.RolePermissionEntity;
import com.jinelei.numbfish.authorization.entity.UserEntity;
import com.jinelei.numbfish.authorization.entity.UserRoleEntity;
import com.jinelei.numbfish.authorization.mapper.UserMapper;
import com.jinelei.numbfish.authorization.service.PermissionService;
import com.jinelei.numbfish.authorization.service.RolePermissionService;
import com.jinelei.numbfish.authorization.service.RoleService;
import com.jinelei.numbfish.authorization.service.UserRoleService;
import com.jinelei.numbfish.authorization.service.UserService;
import com.jinelei.numbfish.common.exception.InvalidArgsException;

@SuppressWarnings("unused")
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity>
        implements UserService, UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final AuthorizationProperty property;
    private final TokenCacheKeyGenerator cacheKeyGenerator = TokenCacheKeyGenerator.defaultGenerator();

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

    public UserServiceImpl(AuthorizationProperty property) {
        this.property = property;
    }

    private final Function<UserQueryRequest, LambdaQueryWrapper<UserEntity>> buildQueryWrapper = (UserQueryRequest request) -> {
        LambdaQueryWrapper<UserEntity> wrapper = Wrappers.lambdaQuery(UserEntity.class);
        wrapper.eq(Objects.nonNull(request.getId()), UserEntity::getId, request.getId());
        wrapper.like(Objects.nonNull(request.getUsername()), UserEntity::getUsername, request.getUsername());
        wrapper.like(Objects.nonNull(request.getNickname()), UserEntity::getNickname, request.getNickname());
        wrapper.like(Objects.nonNull(request.getPhone()), UserEntity::getPhone, request.getPhone());
        wrapper.like(Objects.nonNull(request.getEmail()), UserEntity::getEmail, request.getEmail());
        wrapper.like(Objects.nonNull(request.getAvatar()), UserEntity::getAvatar, request.getAvatar());
        wrapper.like(Objects.nonNull(request.getRemark()), UserEntity::getRemark, request.getRemark());
        return wrapper;
    };

    @Override
    public void create(UserCreateRequest request) {
        final UserEntity entity = userConvertor.requestToEntity(request);
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
                    final List<UserRoleEntity> userRoleEntities = list.stream()
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
        final String username = Optional.ofNullable(request).map(UserUpdateRequest::getUsername)
                .orElseThrow(() -> new InvalidArgsException("用户名不能为空"));
        LambdaUpdateWrapper<UserEntity> wrapper = Wrappers.lambdaUpdate(UserEntity.class);
        wrapper.eq(UserEntity::getId, request.getId());
        wrapper.set(UserEntity::getUsername, request.getUsername());
        Optional.of(request).map(UserUpdateRequest::getPassword).ifPresent(password -> {
            if (password.length() < 6) {
                throw new InvalidArgsException("密码长度不能小于6位");
            }
            wrapper.set(UserEntity::getPassword, passwordEncoder.encode(password));
        });
        wrapper.set(UserEntity::getAvatar, request.getAvatar());
        wrapper.set(UserEntity::getEmail, request.getEmail());
        wrapper.set(UserEntity::getPhone, request.getPhone());
        wrapper.set(UserEntity::getRemark, request.getRemark());
        int updated = baseMapper.update(wrapper);
        Assert.state(updated == 1, "用户更新失败");
        log.debug("更新用户: {}", updated);
        int deleted = userRoleService.getBaseMapper()
                .delete(Wrappers.lambdaUpdate(UserRoleEntity.class).eq(UserRoleEntity::getUserId, request.getId()));
        log.debug("删除用户角色关系: {}", deleted);
        Optional.ofNullable(request.getRoleIds())
                .filter(i -> !CollectionUtils.isEmpty(i))
                .ifPresent(list -> {
                    final List<UserRoleEntity> userRoleEntities = list.stream()
                            .map(id -> {
                                UserRoleEntity userRole = new UserRoleEntity();
                                userRole.setUserId(request.getId());
                                userRole.setRoleId(id);
                                return userRole;
                            }).toList();
                    List<BatchResult> insert = userRoleService.getBaseMapper().insert(userRoleEntities);
                    log.debug("创建用户角色关系: {}", insert.size());
                });
    }

    @Override
    public UserEntity get(UserQueryRequest request) {
        LambdaQueryWrapper<UserEntity> wrapper = buildQueryWrapper.apply(request);
        UserEntity userEntity = baseMapper.selectOne(wrapper);
        Optional.ofNullable(userEntity)
                .map(BaseEntity::getId)
                .map(id -> userRoleService.getBaseMapper().selectList(Wrappers.lambdaQuery(UserRoleEntity.class).eq(UserRoleEntity::getUserId, id)))
                .map(list -> list.stream().map(UserRoleEntity::getRoleId).distinct().collect(Collectors.toList()))
                .map(ids -> roleService.getBaseMapper().selectByIds(ids))
                .ifPresent(list -> {
                    userEntity.setRoles(list);
                    userEntity.setRoleIds(Optional.ofNullable(userEntity.getRoles()).stream().flatMap(Collection::stream).map(RoleEntity::getId).collect(Collectors.toList()));
                });
        return userEntity;
    }

    @Override
    public List<UserEntity> list(UserQueryRequest request) {
        LambdaQueryWrapper<UserEntity> wrapper = buildQueryWrapper.apply(request);
        List<UserEntity> userEntities = baseMapper.selectList(wrapper);
        Optional.ofNullable(userEntities)
                .map(l -> l.stream().map(BaseEntity::getId).toList())
                .map(ids -> userRoleService.getBaseMapper().selectList(Wrappers.lambdaQuery(UserRoleEntity.class).in(UserRoleEntity::getUserId, ids)))
                .map(l -> {
                    List<Long> ids = l.stream().map(UserRoleEntity::getRoleId).toList();
                    List<RoleEntity> roleEntities = roleService.getBaseMapper().selectByIds(ids);
                    return l.stream().collect(Collectors.groupingBy(UserRoleEntity::getRoleId, Collectors.mapping(e ->
                            roleEntities.stream().filter(p -> p.getId().equals(e.getRoleId())).findFirst().orElse(null), Collectors.toList())));
                })
                .ifPresent(map -> userEntities.forEach(e -> {
                    e.setRoles(map.get(e.getId()));
                    e.setRoleIds(Optional.ofNullable(e.getRoles()).stream().flatMap(Collection::stream).map(RoleEntity::getId).collect(Collectors.toList()));
                }));
        return userEntities;
    }

    @Override
    public IPage<UserEntity> page(IPage<UserEntity> page, UserQueryRequest request) {
        LambdaQueryWrapper<UserEntity> wrapper = buildQueryWrapper.apply(request);
        IPage<UserEntity> result = baseMapper.selectPage(page, wrapper);
        Optional.ofNullable(result.getRecords())
                .map(l -> l.stream().map(BaseEntity::getId).toList())
                .filter(list -> !CollectionUtils.isEmpty(list))
                .map(ids -> userRoleService.getBaseMapper().selectList(Wrappers.lambdaQuery(UserRoleEntity.class).in(UserRoleEntity::getUserId, ids)))
                .filter(list -> !CollectionUtils.isEmpty(list))
                .map(l -> {
                    List<Long> ids = l.stream().map(UserRoleEntity::getRoleId).toList();
                    List<RoleEntity> roleEntities = roleService.getBaseMapper().selectByIds(ids);
                    return l.stream().collect(Collectors.groupingBy(UserRoleEntity::getUserId, Collectors.mapping(e ->
                            roleEntities.stream().filter(p -> p.getId().equals(e.getRoleId())).findFirst().orElse(null), Collectors.toList())));
                })
                .ifPresent(map -> result.getRecords().forEach(e -> {
                    e.setRoles(map.get(e.getId()));
                    e.setRoleIds(Optional.ofNullable(e.getRoles()).stream().flatMap(Collection::stream).map(RoleEntity::getId).collect(Collectors.toList()));
                }));
        return result;
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
        final String keyUserTokenInfo = cacheKeyGenerator.apply(token);
        redisTemplate.opsForHash().put(keyUserTokenInfo, "username", userEntity.getUsername());
        redisTemplate.opsForHash().put(keyUserTokenInfo, "id", userEntity.getId().toString());
        stringRedisTemplate.expire(keyUserTokenInfo, Duration.ofMinutes(30));
        log.debug("用户登录: {}", userEntity);
        return token;
    }

    @Override
    public void logout() {
        log.debug("用户登出");
        Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(a -> a instanceof TokenAuthenticationToken)
                .map(TokenAuthenticationToken.class::cast)
                .map(TokenAuthenticationToken::getToken)
                .ifPresentOrElse(token -> stringRedisTemplate.delete(cacheKeyGenerator.apply(token)),
                        () -> {
                            throw new InvalidArgsException("用户未登录");
                        });
    }

    @Override
    public void updatePassword(UserUpdatePasswordRequest request) {
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
    public UserInfoResponse info() {
        final Authentication authentication = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .orElseThrow(() -> new InvalidArgsException("用户未登录"));
        if (authentication instanceof TokenAuthenticationToken token) {
            UserEntity userEntity = getById(token.getUserId());
            UserInfoResponse userInfoResponse = userConvertor.entityToInfo(userEntity);
            List<RoleEntity> roleByUser = getRoleListByUserId(userEntity);
            List<PermissionEntity> permissionByUser = getPermissionListByUserId(userEntity, roleByUser);
            userInfoResponse.setRoles(roleService.convertTree(roleByUser));
            userInfoResponse.setPermissions(permissionConvertor.entityToResponse(permissionByUser));
            return userInfoResponse;
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<UserEntity> wrapper = Wrappers.lambdaQuery(UserEntity.class);
        wrapper.eq(UserEntity::getUsername, username);
        UserEntity userEntity = baseMapper.selectOne(wrapper);
        Optional.ofNullable(userEntity).orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
        final Collection<GrantedAuthority> authorities = new ArrayList<>();
        List<RoleEntity> roleByUser = getRoleListByUserId(userEntity);
        List<PermissionEntity> permissionByUser = getPermissionListByUserId(userEntity, roleByUser);
        roleByUser.stream().map(RoleEntity::getCode).map(i -> "ROLE_" + i).map(SimpleGrantedAuthority::new)
                .forEach(authorities::add);
        permissionByUser.stream().map(PermissionEntity::getCode).map(SimpleGrantedAuthority::new)
                .forEach(authorities::add);
        return new User(userEntity.getUsername(), userEntity.getPassword(), authorities);
    }

    private Boolean isAdmin(UserEntity user) {
        final String username = Optional.ofNullable(user).map(UserEntity::getUsername)
                .orElseThrow(() -> new InvalidArgsException("用户不存在"));
        return Optional.ofNullable(property).map(AuthorizationProperty::getAdmin).map(AdminProperty::getUsername)
                .map(username::equals).orElse(false);
    }

    private List<RoleEntity> getRoleListByUserId(UserEntity user) {
        final Boolean isAdmin = isAdmin(user);
        final Set<Long> roleIds = new HashSet<>();
        if (isAdmin) {
            final List<RoleEntity> roleEntities = roleService.list(Wrappers.lambdaQuery(RoleEntity.class));
            roleEntities.stream().map(BaseEntity::getId).forEach(roleIds::add);
        } else {
            // 查询用户关联的所有的角色
            final List<UserRoleEntity> userRoleEntities = userRoleService
                    .list(Wrappers.lambdaQuery(UserRoleEntity.class).eq(UserRoleEntity::getUserId, user.getId()));
            userRoleEntities.stream().map(UserRoleEntity::getRoleId).forEach(roleIds::add);
        }
        List<RoleEntity> allRoles = ((RoleMapper) roleService.getBaseMapper())
                .selectTree(new ArrayList<>(roleIds), TreeBuildMode.CHILD_AND_CURRENT);
        return allRoles;
    }

    private List<PermissionEntity> getPermissionListByUserId(UserEntity user, List<RoleEntity> roles) {
        final Boolean isAdmin = isAdmin(user);
        final Set<Long> permissionIds = new HashSet<>();
        if (isAdmin) {
            final List<PermissionEntity> permissionEntities = permissionService
                    .list(Wrappers.lambdaQuery(PermissionEntity.class));
            permissionEntities.stream().map(BaseEntity::getId).forEach(permissionIds::add);
        } else {
            // 查询用户关联的所有的角色
            if (!roles.isEmpty()) {
                List<Long> roleIds = roles.stream().map(BaseEntity::getId).toList();
                final List<RolePermissionEntity> rolePermissionEntities = rolePermissionService.list(
                        Wrappers.lambdaQuery(RolePermissionEntity.class).in(RolePermissionEntity::getRoleId, roleIds));
                rolePermissionEntities.stream().map(RolePermissionEntity::getPermissionId)
                        .forEach(permissionIds::add);
            }
        }
        List<PermissionEntity> allPermissions = ((PermissionMapper) permissionService.getBaseMapper())
                .selectTree(new ArrayList<>(permissionIds), TreeBuildMode.CHILD_AND_CURRENT);
        return allPermissions;
    }

}
