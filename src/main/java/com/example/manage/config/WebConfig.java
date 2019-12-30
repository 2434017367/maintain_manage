/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.example.manage.config;

import com.example.manage.interceptor.LoginHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc配置
 *
 * @author Mark sunlightcs@gmail.com
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/statics/**").addResourceLocations("classpath:/statics/");
        registry.addResourceHandler("/public/**").addResourceLocations("classpath:/public/");

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor())
                .addPathPatterns("/toMain",
                                "/toForm",
                                "/api/**",
                                "/to/**",
                                "/toAdmin/**",
                                "/toEmp/**")
                .excludePathPatterns("/api/user/login",
                                    "/api/user/registered",
                                    "/api/emp/login",
                                    "/api/admin/login",
                                    "/api/apply/**",
                                    "/toForm/apply_order",
                                    "/api/user/rePass");

    }
}