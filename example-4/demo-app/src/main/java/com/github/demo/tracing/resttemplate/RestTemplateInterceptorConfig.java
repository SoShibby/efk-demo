package com.github.demo.tracing.resttemplate;

import com.uber.jaeger.Tracer;
import io.opentracing.contrib.spring.web.client.RestTemplateSpanDecorator;
import io.opentracing.contrib.spring.web.starter.client.TracingRestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RestTemplateInterceptorConfig {

    @Bean
    public TracingRestTemplateCustomizer tracingRestTemplateCustomizer(Tracer tracer, List<RestTemplateSpanDecorator> spanDecorators) {
        return new RestTemplateCustomizer(tracer, spanDecorators);
    }
}
