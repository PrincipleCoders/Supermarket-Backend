package com.principlecoders.orderservice.configs;

import com.principlecoders.common.filters.ApiSecretFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.principlecoders.common.utils.ServiceApiKeys.ORDER_API_KEY;

@Configuration
public class ApiSecurityConfig {
    @Bean
    public FilterRegistrationBean<ApiSecretFilter> apiSecretFilter() {
        FilterRegistrationBean<ApiSecretFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ApiSecretFilter(ORDER_API_KEY));
        registrationBean.addUrlPatterns("order/*");
        return registrationBean;
    }
}
