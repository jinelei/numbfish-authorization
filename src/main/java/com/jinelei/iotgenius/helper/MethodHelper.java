package com.jinelei.iotgenius.helper;

import org.springframework.util.ObjectUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: jinelei
 * @Description:
 * @Date: 2023/7/13
 * @Version: 1.0.0
 */
@SuppressWarnings("unused")
public class MethodHelper {
    public static Predicate<Method> isPublicMethod = m -> Modifier.isPublic(m.getModifiers());
    public static Predicate<Method> isGetterMethod = m -> m.getParameterCount() == 0 && !m.getName().equals("getClass") && (m.getName().startsWith("get") || m.getName().startsWith("is"));
    public static Function<Method, String> methodToField = m -> m.getName().startsWith("get") && m.getName().length() > 3 ? m.getName().substring(3, 4).toLowerCase() + m.getName().substring(4) : m.getName();
    /**
     * 当前类型
     */
    private final Class<?> curClazz;
    /**
     * 截止类型
     */
    private final Class<?> endClazz;

    public MethodHelper(Class<?> curClazz, Class<?> endClazz) {
        this.curClazz = curClazz;
        this.endClazz = endClazz;
    }

    public static MethodHelper of(Class<?> curClazz, Class<?> endClazz) {
        return new MethodHelper(curClazz, endClazz);
    }

    /**
     * 解析所有的方法
     *
     * @return 字段列表
     */
    public Stream<Method> getPublicMethods() {
        return Arrays.stream(curClazz.getMethods());
    }

    /**
     * 解析所有的方法
     *
     * @param predicate 判断条件
     * @return 字段列表
     */
    public List<Method> getPublicMethods(final Predicate<Method> predicate) {
        return Arrays.stream(curClazz.getMethods()).filter(predicate).collect(Collectors.toList());
    }

    /**
     * 递归所有的方法
     *
     * @param predicate 判断条件
     * @return 字段列表
     */
    public List<Method> getRecursionMethods(final Predicate<Method> predicate) {
        final List<Method> fields = new ArrayList<>();
        Class<?> clazz = curClazz;
        while (true) {
            Optional.ofNullable(clazz).map(Class::getDeclaredMethods).stream().flatMap(Arrays::stream).filter(predicate).forEach(fields::add);
            if (clazz == endClazz || clazz == null) break;
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    /**
     * 解析方法值
     *
     * @param method 方法
     * @param query  对象
     * @return 方法的值
     */
    public final Optional<Object> getMethodValue(Method method, Object query) {
        try {
            return Optional.ofNullable(method.invoke(query)).filter(o -> !ObjectUtils.isEmpty(o));
        } catch (IllegalAccessException | InvocationTargetException e) {
            return Optional.empty();
        }
    }
}