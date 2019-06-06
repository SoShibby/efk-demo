package com.github.demo.tracing.servlet;

import com.github.demo.tracing.servlet.interceptors.ServletInterceptor;
import com.uber.jaeger.Tracer;
import io.opentracing.contrib.spring.web.interceptor.HandlerInterceptorSpanDecorator;
import io.opentracing.contrib.spring.web.interceptor.TracingHandlerInterceptor;
import io.opentracing.contrib.web.servlet.filter.TracingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Arrays;
import java.util.List;

@Configuration
public class ServletInterceptorConfig {

    @Bean
    public FilterRegistrationBean filterRegistrationBean(Tracer tracer) {
        TracingFilter tracingFilter = new TracingFilter(tracer);

        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(tracingFilter);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(Integer.MIN_VALUE);
        filterRegistrationBean.setAsyncSupported(true);

        return filterRegistrationBean;
    }

    @Bean
    public WebMvcConfigurerAdapter tracingHandlerInterceptor(final Tracer tracer) {
        return new WebMvcConfigurerAdapter() {
            public void addInterceptors(InterceptorRegistry registry) {
                List<HandlerInterceptorSpanDecorator> decorators = Arrays.asList(new HandlerInterceptorSpanDecorator[]{HandlerInterceptorSpanDecorator.STANDARD_LOGS, HandlerInterceptorSpanDecorator.HANDLER_METHOD_OPERATION_NAME});

                registry.addInterceptor(new TracingHandlerInterceptor(tracer, decorators));
                registry.addInterceptor(new ServletInterceptor(tracer, decorators));

                super.addInterceptors(registry);
            }
        };
    }

    @Bean
    public TracingFilter tracingFilter() {
        return new TracingFilter();
    }
}
