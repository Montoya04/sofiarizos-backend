package com.sofiarizos.backend.config;

import com.sofiarizos.backend.security.RateLimitFilter;
import com.sofiarizos.backend.security.UserAgentFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<RateLimitFilter> rateLimitFilter() {
        FilterRegistrationBean<RateLimitFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new RateLimitFilter());
        bean.addUrlPatterns("/api/*");
        bean.setOrder(1);
        return bean;
    }

    @Bean
    public FilterRegistrationBean<UserAgentFilter> userAgentFilter() {
        FilterRegistrationBean<UserAgentFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new UserAgentFilter());
        bean.addUrlPatterns("/api/*");
        bean.setOrder(2);
        return bean;
    }
}
