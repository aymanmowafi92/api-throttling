package com.atypon.apithrottling.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.atypon.apithrottling.filters.ThrottlingFilter;

@org.springframework.context.annotation.Configuration
public class Configuration {

	@Bean
	public FilterRegistrationBean<ThrottlingFilter> loggingFilter(){
	    FilterRegistrationBean<ThrottlingFilter> registrationBean = new FilterRegistrationBean<>();

	    registrationBean.setFilter(new ThrottlingFilter());
	    registrationBean.addUrlPatterns("/api-throttling/service1");
	    registrationBean.addUrlPatterns("/api-throttling/service2");
	    registrationBean.setName("ThrottlingFilter");
	    registrationBean.setOrder(1);
	    
	    return registrationBean;
	}
}
