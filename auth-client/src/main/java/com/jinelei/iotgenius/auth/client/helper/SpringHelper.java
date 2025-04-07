package com.jinelei.iotgenius.auth.client.helper;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@SuppressWarnings("unused")
@Component
public class SpringHelper implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    @SuppressWarnings("all")
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringHelper.applicationContext = applicationContext;
    }

    /**
     * 根据Bean名称获取Bean实例
     *
     * @param name Bean名称
     * @return Bean实例
     */
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    /**
     * 根据Bean类型获取Bean实例
     *
     * @param clazz Bean类型
     * @param <T>   Bean类型泛型
     * @return Bean实例
     */
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    /**
     * 根据Bean名称和类型获取Bean实例
     *
     * @param name  Bean名称
     * @param clazz Bean类型
     * @param <T>   Bean类型泛型
     * @return Bean实例
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }

    /**
     * 判断容器中是否包含指定名称的Bean
     *
     * @param name Bean名称
     * @return 如果包含则返回true，否则返回false
     */
    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    /**
     * 判断指定名称的Bean是否为单例
     *
     * @param name Bean名称
     * @return 如果是单例则返回true，否则返回false
     */
    public static boolean isSingleton(String name) {
        return applicationContext.isSingleton(name);
    }

    /**
     * 获取指定名称Bean的类型
     *
     * @param name Bean名称
     * @return Bean类型
     */
    public static Class<?> getType(String name) {
        return applicationContext.getType(name);
    }

    /**
     * 获取激活的配置
     *
     * @return 激活的配置
     */
    public static String[] getActiveProfiles() {
        return applicationContext.getEnvironment().getActiveProfiles();
    }

    /**
     * 是否包含指定配置
     *
     * @param profile 配置
     * @return 如果包含则返回true，否则返回false
     */
    public static boolean isActiveProfile(String profile) {
        for (String activeProfile : getActiveProfiles()) {
            if (activeProfile.equals(profile)) {
                return true;
            }
        }
        return false;
    }
}
