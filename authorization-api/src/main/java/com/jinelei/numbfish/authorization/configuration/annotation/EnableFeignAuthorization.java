package com.jinelei.numbfish.authorization.configuration.annotation;

import com.jinelei.numbfish.authorization.configuration.FeignAuthorizationConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(FeignAuthorizationConfiguration.class)
public @interface EnableFeignAuthorization {
}
