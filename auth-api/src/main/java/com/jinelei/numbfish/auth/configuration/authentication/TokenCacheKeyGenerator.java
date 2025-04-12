package com.jinelei.numbfish.auth.configuration.authentication;

import java.util.function.Function;

/**
 * Token缓存key生成器
 */
@SuppressWarnings("unused")
@FunctionalInterface
public interface TokenCacheKeyGenerator extends Function<String, String> {

    static TokenCacheKeyGenerator defaultGenerator() {
        return token -> "user:token:info:" + token;
    }

}
