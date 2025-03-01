package com.jinelei.iotgenius.helper;


import com.jinelei.iotgenius.exception.InternalException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: jinelei
 * @Description:
 * @Date: 2023/7/23
 * @Version: 1.0.0
 */
@SuppressWarnings({"unchecked", "unused"})
public interface ClassService<T1, T2, T3, T4> {
    Map<Object, Type[]> cachedTypes = new ConcurrentHashMap<>();

    /**
     * 获取泛型查询参数类型
     *
     * @return 泛型参数类型
     */
    default Class<T1> classT1() {
        Type[] types = cachedTypes.computeIfAbsent(this, k -> getArgumentType(this.getClass()));
        return (Class<T1>) types[0];
    }

    /**
     * 获取泛型查询参数实例
     *
     * @return 泛型参数实例
     */
    default T1 instanceT1() {
        try {
            return classT1().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                 | NoSuchMethodException e) {
            throw new InternalException("get first instance error");
        }
    }

    /**
     * 获取泛型实体参数类型
     *
     * @return 泛型参数类型
     */
    default Class<T2> classT2() {
        Type[] types = cachedTypes.computeIfAbsent(this, k -> getArgumentType(this.getClass()));
        return (Class<T2>) types[1];
    }

    /**
     * 获取泛型实体参数实例
     *
     * @return 泛型参数实例
     */
    default T2 instanceT2() {
        try {
            return classT2().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                 | NoSuchMethodException e) {
            throw new InternalException("get second instance error");
        }
    }

    /**
     * 获取泛型实体参数类型
     *
     * @return 泛型参数类型
     */
    default Class<T3> classT3() {
        Type[] types = cachedTypes.computeIfAbsent(this, k -> getArgumentType(this.getClass()));
        return (Class<T3>) types[2];
    }

    /**
     * 获取泛型实体参数实例
     *
     * @return 泛型参数实例
     */
    default T3 instanceT3() {
        try {
            return classT3().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                 | NoSuchMethodException e) {
            throw new InternalException("get third instance error");
        }
    }

    /**
     * 获取泛型实体参数类型
     *
     * @return 泛型参数类型
     */
    default Class<T4> classT4() {
        Type[] types = cachedTypes.computeIfAbsent(this, k -> getArgumentType(this.getClass()));
        return (Class<T4>) types[3];
    }

    /**
     * 获取泛型实体参数实例
     *
     * @return 泛型参数实例
     */
    default T4 instanceT4() {
        try {
            return classT4().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                 | NoSuchMethodException e) {
            throw new InternalException("get third instance error");
        }
    }

    /**
     * 获取泛型参数类型
     *
     * @param clazz 泛型参数类型
     * @return 泛型参数类型
     */
    private Type[] getArgumentType(Class<?> clazz) {
        if (clazz == null) {
            return new Type[0];
        }
        return Optional.of(clazz)
                .map(Class::getGenericSuperclass)
                .filter(it -> it instanceof ParameterizedType)
                .map(it -> (ParameterizedType) it)
                .map(ParameterizedType::getActualTypeArguments)
                .filter(it -> it.length > 0)
                .orElseGet(() -> getArgumentType(clazz.getSuperclass()));
    }
}
