package com.principlecoders.deliveryservice.configs;

import com.principlecoders.common.filters.ApiSecretFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.principlecoders.common.utils.ServiceApiKeys.DELIVERY_API_KEY;

@Configuration
public class ApiSecurityConfig {
    @Bean
    public FilterRegistrationBean<ApiSecretFilter> apiSecretFilter() {
        FilterRegistrationBean<ApiSecretFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ApiSecretFilter(DELIVERY_API_KEY));
        registrationBean.addUrlPatterns("delivery/*");
        return registrationBean;
    }
}
