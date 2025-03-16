package com.jinelei.iotgenius.auth.permission.declaration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import com.jinelei.iotgenius.auth.enumeration.PermissionType;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "权限声明对象")
public interface PermissionDeclaration {
    public static final Logger log = LoggerFactory.getLogger(PermissionDeclaration.class);

    @Schema(description = "权限编码")
    public String getCode();

    @Schema(description = "权限描述")
    public String getDescription();

    @Schema(description = "权限类型")
    public PermissionType getType();

    @SuppressWarnings("unchecked")
    public static List<PermissionDeclaration> getPermissions(String basePackage) {
        final List<PermissionDeclaration> permissions = new ArrayList<>();
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AssignableTypeFilter(PermissionDeclaration.class));
        Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(basePackage);
        for (BeanDefinition candidateComponent : candidateComponents) {
            String className = candidateComponent.getBeanClassName();
            if (className != null) {
                try {
                    Class<PermissionDeclaration> clazz = (Class<PermissionDeclaration>) Class.forName(className);
                    permissions.add(clazz.getDeclaredConstructor().newInstance());
                } catch (Throwable e) {
                    log.error("Failed to load class: {}", className);
                }
            }
        }
        return permissions;
    }

}
