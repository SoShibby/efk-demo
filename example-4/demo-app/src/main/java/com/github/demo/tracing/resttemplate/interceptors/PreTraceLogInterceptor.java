package com.github.demo.tracing.resttemplate.interceptors;

import com.uber.jaeger.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.contrib.spring.web.client.RestTemplateSpanDecorator;
import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.List;

public class PreTraceLogInterceptor implements ClientHttpRequestInterceptor {

    private Tracer tracer;
    private List<RestTemplateSpanDecorator> spanDecorators;

    public PreTraceLogInterceptor(Tracer tracer, List<RestTemplateSpanDecorator> spanDecorators) {
        this.tracer = tracer;
        this.spanDecorators = spanDecorators;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        SpanContext context = (SpanContext) tracer.scopeManager().active().span().context();

        String traceId = String.format("%x", context.getTraceId());
        String spanId = String.format("%x", context.getSpanId());
        String parentSpanId = String.format("%x", context.getParentId());

        ClientHttpResponse response;

        try {
            response = execution.execute(request, body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        MDC.put("trace-id", traceId);
        MDC.put("span-id", spanId);
        MDC.put("parent-span-id", parentSpanId);

        return response;
    }
}
