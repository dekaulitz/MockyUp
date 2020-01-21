package com.github.dekaulitz.mockyup.filters;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class Interceptors implements WebMvcConfigurer {
    @Autowired
    RequestFilters requestFilters;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestFilters);

    }
}
