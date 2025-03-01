package com.jinelei.iotgenius.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @Author: jinelei
 * @Description:
 * @Date: 2023/4/11
 * @Version: 1.0.0
 */
@SuppressWarnings("unused")
@Component
public class SpringHelper implements ApplicationContextAware {
    protected static final Logger logger = LoggerFactory.getLogger(SpringHelper.class);
    protected static ApplicationContext context = null;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * 获取beans
     *
     * @param clazz bean class
     * @param <T>   bean class
     * @return bean
     */
    public static <T> Optional<Collection<T>> getBeans(@NonNull Class<T> clazz) {
        return Optional.ofNullable(context).map(c -> c.getBeansOfType(clazz).values());
    }

    /**
     * 获取bean
     *
     * @param clazz bean class
     * @param <T>   bean class
     * @return bean
     */
    public static <T> Optional<T> getBean(@NonNull Class<T> clazz) {
        return Optional.ofNullable(context).map(c -> c.getBean(clazz));
    }

    /**
     * 获取bean
     *
     * @param name  bean name
     * @param clazz bean class
     * @param <T>   bean class
     * @return bean
     */
    public static <T> Optional<T> getBean(@NonNull String name, @NonNull Class<T> clazz) {
        return Optional.ofNullable(context).map(c -> c.getBean(name, clazz));
    }

    /**
     * 获取bean
     *
     * @param beanName bean name
     * @return bean
     * @throws BeansException bean exception
     */
    public static Optional<Object> getBeanByName(@NonNull String beanName) throws BeansException {
        return Optional.ofNullable(context).map(c -> c.getBean(beanName));
    }

    /**
     * 通过注解获取beanNames
     *
     * @param clazz 注解类型
     * @return beanNames
     */
    public static Optional<List<String>> getBeanNamesForAnnotation(Class<? extends Annotation> clazz) {
        return Optional.ofNullable(context).map(c -> c.getBeanNamesForAnnotation(clazz)).map(List::of);
    }

    /**
     * 获取项目路径
     *
     * @return 路径
     */
    public static String getContextPath() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getContextPath();
    }
}
