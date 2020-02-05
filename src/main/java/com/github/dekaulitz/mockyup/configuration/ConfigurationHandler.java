package com.github.dekaulitz.mockyup.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class ConfigurationHandler implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedOrigins("*")
                .allowedMethods("GET", "PUT", "POST", "PATCH", "DELETE", "OPTIONS");

    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//       ​registry.addResourceHandler("/documentation/swagger-ui.html**").addResourceLocations("classpath:/META-INF/swagger-ui.html");
//       registry.addResourceHandler("/documentation/webjars/**").addResourceLocations("classpath:/META-INF/webjars/");
////               ​registry.​addResourceHandler("/documentation/webjars/**").add.addResourceLocations();
//    }
//
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        ​registry.addRedirectViewController("/documentation/v2/api-docs", "/v2/api-docs?group=restful-api");
//               ​registry.addRedirectViewController("/documentation/swagger-resources/configuration/ui","/swagger-resources/configuration/ui");
//               ​registry.addRedirectViewController("/documentation/swagger-resources/configuration/security","/swagger-resources/configuration/security");
//               ​registry.addRedirectViewController("/documentation/swagger-resources", "/swagger-resources");
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/docs-swagger/**").addResourceLocations(
                "classpath:/public/");
    }
}
