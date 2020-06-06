package com.github.dekaulitz.mockyup.configuration.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import java.util.Collections;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    protected Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SecurityEntryPoint entryPoint;
    @Autowired
    private SecurityProvider securityProvider;

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(securityProvider));
    }

    @Bean
    public SecurityAuthenticationFilter authenticationTokenFilter() {
        SecurityAuthenticationFilter filter = new SecurityAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager());
        filter.setRequiresAuthenticationRequestMatcher(new OrRequestMatcher(new OrRequestMatcher(
                new AntPathRequestMatcher("/mocks/page"),
                new AntPathRequestMatcher("/mocks/**/update"),
                new AntPathRequestMatcher("/mocks/**/delete"),
                new AntPathRequestMatcher("/mocks/store"),
                new AntPathRequestMatcher("/mocks/**/detail"),
                new AntPathRequestMatcher("/mocks/**/users"),
                new AntPathRequestMatcher("/mocks/**/spec"),
                new AntPathRequestMatcher("/mocks/**/histories"),
                new AntPathRequestMatcher("/mocks/**/detailWithAccess"),
                new AntPathRequestMatcher("/mocks/**/addUser"),
                new AntPathRequestMatcher("/mocks/**/remove/**"),
                new AntPathRequestMatcher("/mocks/users"),
                new AntPathRequestMatcher("/mocks/users/list"),
                new AntPathRequestMatcher("/mocks/user/**/update"),
                new AntPathRequestMatcher("/mocks/user/**/detail")
        )));
        filter.setAuthenticationSuccessHandler(new SecurityAuthenticationSuccess());
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info(http.toString());
        http.csrf().disable().cors().and()
                .formLogin().disable()
                .exceptionHandling().authenticationEntryPoint(entryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.headers().frameOptions().sameOrigin();
    }
}
