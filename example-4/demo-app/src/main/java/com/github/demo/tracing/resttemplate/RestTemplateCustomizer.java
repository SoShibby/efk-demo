package com.github.demo.tracing.resttemplate;

import com.github.demo.tracing.resttemplate.interceptors.PreTraceLogInterceptor;
import com.github.demo.tracing.resttemplate.interceptors.TraceLogInterceptor;
import io.opentracing.Tracer;
import io.opentracing.contrib.spring.web.client.RestTemplateSpanDecorator;
import io.opentracing.contrib.spring.web.starter.client.TracingRestTemplateCustomizer;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class RestTemplateCustomizer extends TracingRestTemplateCustomizer {

    private Tracer tracer;
    private List<RestTemplateSpanDecorator> spanDecorators;

    public RestTemplateCustomizer(Tracer tracer, List<RestTemplateSpanDecorator> spanDecorators) {
        super(tracer, spanDecorators);
        this.tracer = tracer;
        this.spanDecorators = spanDecorators;
    }

    @Override
    public void customize(RestTemplate restTemplate) {
        restTemplate.getInterceptors()
                .add(new PreTraceLogInterceptor(tracer, spanDecorators));

        super.customize(restTemplate);

        restTemplate.getInterceptors()
                .add(new TraceLogInterceptor(tracer, spanDecorators));
    }
}
