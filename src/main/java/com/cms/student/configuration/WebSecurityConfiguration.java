package com.cms.student.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfiguration {
    private final String key;

    public WebSecurityConfiguration(@Value("${security.key}")String key) {
        this.key = key;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(new JwtValidator(key), BasicAuthenticationFilter.class)
                .csrf().disable()
                .authorizeRequests(
                        authorize -> {
                            authorize.anyRequest().authenticated();
                        }
                ).formLogin().and()
                .httpBasic();
        return http.build();
    }


}
