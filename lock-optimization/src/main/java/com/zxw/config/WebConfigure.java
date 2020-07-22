package com.zxw.config;

import com.zxw.filter.RouteFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zxw
 * @date 2020/7/21 17:05
 */
@Configuration
public class WebConfigure implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RouteFilter());
    }
}
