package com.jinelei.iotgenius.helper;

import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
/**
 * @Author: jinelei
 * @Description:
 * @Date: 2023/7/13
 * @Version: 1.0.0
 */
public class FieldHelper {
    /**
     * 当前类型
     */
    private final Class<?> curClazz;
    /**
     * 截止类型
     */
    private final Class<?> endClazz;
    public FieldHelper(Class<?> curClazz, Class<?> endClazz) {
        this.curClazz = curClazz;
        this.endClazz = endClazz;
    }
    public static FieldHelper of(Class<?> curClazz, Class<?> endClazz) {
        return new FieldHelper(curClazz, endClazz);
    }
    /**
     * 解析所有的字段
     *
     * @param predicate 判断条件
     * @return 字段列表
     */
    public List<Field> getFields(final Predicate<Field> predicate) {
        final List<Field> fields = new ArrayList<>();
        Class<?> clazz = curClazz;
        while (true) {
            Optional.ofNullable(clazz).map(Class::getDeclaredFields).stream().flatMap(Arrays::stream).filter(predicate).forEach(fields::add);
            if (clazz == endClazz || clazz == null) break;
            clazz = clazz.getSuperclass();
        }
        return fields;
    }
    /**
     * 解析所有的字段
     *
     * @return 字段列表
     */
    public List<Field> getFields() {
        return getFields(f -> true);
    }
    /**
     * 解析字段值
     *
     * @param field 字段
     * @param query 对象
     * @return 字段的值
     */
    public final Optional<Object> getFieldValue(Field field, Object query) {
        try {
            field.setAccessible(true);
            return Optional.ofNullable(field.get(query)).filter(o -> !ObjectUtils.isEmpty(o));
        } catch (IllegalAccessException e) {
            return Optional.empty();
        }
    }
}