package com.jinelei.numbfish.authorization.configuration;

import com.jinelei.numbfish.authorization.dto.ClientResponse;
import com.jinelei.numbfish.authorization.dto.UserResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@SuppressWarnings("unused")
@Configuration
public class RedisConfiguration {

    @Bean(name = "longRedisTemplate")
    public RedisTemplate<String, Long> longRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Long> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // 设置键的序列化器
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        // 设置值的序列化器
        Jackson2JsonRedisSerializer<Long> jsonSerializer = new Jackson2JsonRedisSerializer<>(Long.class);
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);

        // 初始化模板
        template.afterPropertiesSet();
        return template;
    }

    @Bean(name = "userRedisTemplate")
    public RedisTemplate<String, UserResponse> userRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, UserResponse> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // 设置键的序列化器
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        // 设置值的序列化器
        Jackson2JsonRedisSerializer<UserResponse> jsonSerializer = new Jackson2JsonRedisSerializer<>(UserResponse.class);
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);

        // 初始化模板
        template.afterPropertiesSet();
        return template;
    }

    @Bean(name = "clientRedisTemplate")
    public RedisTemplate<String, ClientResponse> clientRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, ClientResponse> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // 设置键的序列化器
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        // 设置值的序列化器
        Jackson2JsonRedisSerializer<ClientResponse> jsonSerializer = new Jackson2JsonRedisSerializer<>(ClientResponse.class);
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);

        // 初始化模板
        template.afterPropertiesSet();
        return template;
    }

}
