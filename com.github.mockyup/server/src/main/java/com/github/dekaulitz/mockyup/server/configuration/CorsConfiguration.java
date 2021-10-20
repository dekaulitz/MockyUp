package com.github.dekaulitz.mockyup.server.configuration;

import java.time.Duration;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web mvc and configuration
 */
@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedHeaders("*")
        .allowedOrigins("*")
        .allowedMethods("GET", "PUT", "POST", "PATCH", "DELETE", "OPTIONS");

  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/mocks/docs-swagger/**").addResourceLocations(
        "classpath:/public/");

    registry.addResourceHandler("/mocks/static/**").addResourceLocations(
        "classpath:templates/dist").setCacheControl(CacheControl.maxAge(Duration.ofSeconds(3600)));
  }

}
