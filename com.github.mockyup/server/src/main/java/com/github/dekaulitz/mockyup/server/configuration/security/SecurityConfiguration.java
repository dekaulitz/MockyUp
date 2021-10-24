package com.github.dekaulitz.mockyup.server.configuration.security;


import static com.github.dekaulitz.mockyup.server.model.constants.ApplicationConstants.PROJECTS;
import static com.github.dekaulitz.mockyup.server.model.constants.ApplicationConstants.PROJECT_CONTRACTS;
import static com.github.dekaulitz.mockyup.server.model.constants.ApplicationConstants.REGEX_PATH;
import static com.github.dekaulitz.mockyup.server.model.constants.ApplicationConstants.USERS;
import static com.github.dekaulitz.mockyup.server.model.constants.ApplicationConstants.V1;

import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Slf4j
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private SecurityEntryPoint entryPoint;
  @Autowired
  private SecurityProvider securityProvider;


  @Bean
  public AuthenticationManager authenticationManager() {
    securityProvider.setPreAuthenticationChecks(new PreAuthenticationChecker());
    return new ProviderManager(Collections.singletonList(securityProvider));
  }

  @Bean
  public SecurityAuthenticationFilter authenticationTokenFilter() {
    SecurityAuthenticationFilter filter = new SecurityAuthenticationFilter();
    filter.setAuthenticationManager(authenticationManager());
    filter.setRequiresAuthenticationRequestMatcher(new OrRequestMatcher(new OrRequestMatcher(
        new AntPathRequestMatcher(V1 + USERS + REGEX_PATH),
        new AntPathRequestMatcher(V1 + USERS),
        new AntPathRequestMatcher(V1 + PROJECTS + REGEX_PATH),
        new AntPathRequestMatcher(V1 + PROJECTS),
        new AntPathRequestMatcher(V1 + PROJECT_CONTRACTS + REGEX_PATH),
        new AntPathRequestMatcher(V1 + PROJECT_CONTRACTS)
    )));
    filter.setAuthenticationSuccessHandler(
        (httpServletRequest, httpServletResponse, authentication) -> {
        });
    filter.setAuthenticationFailureHandler((httpServletRequest, httpServletResponse, e) -> {

    });
    return filter;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable().cors().and()
        .formLogin().disable()
        .exceptionHandling().authenticationEntryPoint(entryPoint)
        .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.headers().frameOptions().sameOrigin();
  }
}
