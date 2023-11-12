package com.principlecoders.apigateway.configs;

//import com.principlecoders.apigateway.filters.SecurityChainFilter;
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
//                .addFilterBefore(new SecurityChainFilter(), BasicAuthenticationFilter.class)
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .cors(cors-> cors
                        .configurationSource(request -> {
                            var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                            corsConfig.addAllowedOrigin("*");
                            corsConfig.addAllowedHeader("*");
                            corsConfig.addAllowedMethod("*");
                            corsConfig.addExposedHeader("Authorization");
                            return corsConfig;
                        })
                )
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().permitAll()
                );
        return http.build();
    }
}

