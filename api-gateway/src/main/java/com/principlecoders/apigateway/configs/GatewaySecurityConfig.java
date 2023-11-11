package com.principlecoders.apigateway.configs;

import com.principlecoders.apigateway.filters.FirebaseTokenValidatorFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class GatewaySecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new FirebaseTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("auth/login/**").permitAll()
                        .requestMatchers("auth/setUserRole/**").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }

}

