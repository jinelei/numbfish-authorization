package com.jinelei.iotgenius.helper;


import com.jinelei.iotgenius.exception.InvalidArgsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @Author: jinelei
 * @Description:
 * @Date: 2023/9/26 20:55
 * @Version: 1.0.0
 */
@SuppressWarnings({"unchecked", "unused"})
public class Builder<T> {
    private final static Logger log = LoggerFactory.getLogger(Builder.class);
    private final AtomicReference<T> instance = new AtomicReference<>();
    private final List<Consumer<T>> modifiers = new ArrayList<>();

    private Builder(final Supplier<T> initializer) {
        this.instance.compareAndSet(null, initializer.get());
    }

    /**
     * 通过构造函数生成Builder
     *
     * @param initializer 构造函数
     * @param <T>         泛型
     * @return Builder
     */
    public static <T> Builder<T> of(Supplier<T> initializer) {
        return new Builder<>(initializer);
    }

    /**
     * 通过对象生成Builder，并且设置对应的属性拷贝
     *
     * @param object 对象
     * @param <T>    泛型
     * @return Builder
     */
    public static <T> Builder<T> toBuilder(final T object) {
        final Builder<T> builder = Builder.of(Arrays.stream(object.getClass().getConstructors())
                .filter(constructor -> constructor.getParameterCount() == 0)
                .findFirst()
                .map(c -> (Supplier<T>) () -> {
                    try {
                        return (T) c.newInstance();
                    } catch (Throwable e) {
                        throw new RuntimeException("不支持的构造函数");
                    }
                })
                .orElseThrow(() -> new InvalidArgsException("不支持的无参构造函数")));
        return builder.copy(object);
    }

    /**
     * 属性拷贝
     *
     * @param object           对象
     * @param ignoreProperties 属性百名单，百名单内的属性不赋值
     * @return Builder
     */
    public Builder<T> copy(final Object object, final List<String> ignoreProperties) {
        final Builder<T> builder = this;
        Optional.ofNullable(object)
                .map(Object::getClass)
                .map(Class::getMethods)
                .stream()
                .flatMap(Stream::of)
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .filter(m -> m.getName().startsWith("get"))
                .filter(m -> m.getParameterCount() == 0)
                .filter(m -> ignoreProperties.stream()
                        .noneMatch(n -> n.equalsIgnoreCase(m.getName().replace("get", ""))))
                .forEach(getMethod -> {
                    final String name = getMethod.getName().replace("get", "set");
                    final Class<?> parameterType = getMethod.getReturnType();
                    try {
                        Optional.ofNullable(getMethod.invoke(object))
                                .ifPresent(val -> Optional.ofNullable(instance.get())
                                        .map(o -> o.getClass())
                                        .map(Class::getMethods)
                                        .stream()
                                        .flatMap(Stream::of)
                                        .filter(m -> Modifier.isPublic(m.getModifiers()))
                                        .filter(m -> m.getParameterCount() == 1)
                                        .filter(m -> m.getName().equals(name))
                                        .filter(m -> m.getParameterTypes()[0].equals(parameterType))
                                        .forEach(setMethod -> {
                                            try {
                                                builder.modifiers.add(instance -> {
                                                    try {
                                                        setMethod.invoke(instance, val);
                                                    } catch (Throwable ignore) {
                                                    }
                                                });
                                            } catch (Throwable throwable) {
                                                log.error("Object convert to Builder failure: {}", throwable.getMessage());
                                            }
                                        }));
                    } catch (Throwable ignore) {
                    }
                });
        return builder;
    }

    /**
     * 属性拷贝
     *
     * @param object 对象
     * @return Builder
     */
    public Builder<T> copy(final Object object) {
        return copy(object, new ArrayList<>());
    }

    public <P1> Builder<T> with(Consumer1<T, P1> consumer, P1 p1) {
        Consumer<T> c = instance -> consumer.accept(instance, p1);
        modifiers.add(c);
        return this;
    }

    public <P1, P2> Builder<T> with(Consumer2<T, P1, P2> consumer, P1 p1, P2 p2) {
        Consumer<T> c = instance -> consumer.accept(instance, p1, p2);
        modifiers.add(c);
        return this;
    }

    public <P1, P2, P3> Builder<T> with(Consumer3<T, P1, P2, P3> consumer, P1 p1, P2 p2, P3 p3) {
        Consumer<T> c = instance -> consumer.accept(instance, p1, p2, p3);
        modifiers.add(c);
        return this;
    }

    public <P1, P2, P3, P4> Builder<T> with(Consumer4<T, P1, P2, P3, P4> consumer, P1 p1, P2 p2, P3 p3, P4 p4) {
        Consumer<T> c = instance -> consumer.accept(instance, p1, p2, p3, p4);
        modifiers.add(c);
        return this;
    }

    public T build() {
        T value = instance.get();
        modifiers.forEach(modifier -> modifier.accept(value));
        modifiers.clear();
        return value;
    }

    public Stream<T> stream() {
        return Stream.of(build());
    }

    public Optional<T> optional() {
        return Optional.of(build());
    }

    @FunctionalInterface
    public interface Consumer1<T, P1> {
        void accept(T t, P1 p1);
    }

    @FunctionalInterface
    public interface Consumer2<T, P1, P2> {
        void accept(T t, P1 p1, P2 p2);
    }

    @FunctionalInterface
    public interface Consumer3<T, P1, P2, P3> {
        void accept(T t, P1 p1, P2 p2, P3 p3);
    }

    @FunctionalInterface
    public interface Consumer4<T, P1, P2, P3, P4> {
        void accept(T t, P1 p1, P2 p2, P3 p3, P4 p4);
    }
}
