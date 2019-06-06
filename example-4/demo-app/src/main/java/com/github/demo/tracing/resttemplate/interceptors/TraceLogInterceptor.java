package com.github.demo.tracing.resttemplate.interceptors;

import com.uber.jaeger.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.contrib.spring.web.client.RestTemplateSpanDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.List;

public class TraceLogInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(TraceLogInterceptor.class);
    private Tracer tracer;
    private List<RestTemplateSpanDecorator> spanDecorators;

    public TraceLogInterceptor(Tracer tracer, List<RestTemplateSpanDecorator> spanDecorators) {
        this.tracer = tracer;
        this.spanDecorators = spanDecorators;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        SpanContext context = (SpanContext) tracer.scopeManager().active().span().context();
        String traceId = String.format("%x", context.getTraceId());
        String spanId = String.format("%x", context.getSpanId());
        String parentSpanId = String.format("%x", context.getParentId());

        MDC.put("trace-id", traceId);
        MDC.put("span-id", spanId);
        MDC.put("parent-span-id", parentSpanId);

        log.info("Sending request to: " + request.getURI());

        ClientHttpResponse response;

        try {
            response = execution.execute(request, body);
        } catch (Exception e) {
            setErrorSpan();
            throw new RuntimeException(e);
        }

        MDC.put("status-code", String.valueOf(response.getStatusCode().value()));
        log.info("Received response: " + response.getStatusCode() + " from " + request.getURI());
        MDC.remove("status-code");

        if (response.getStatusCode().isError()) {
            setErrorSpan();
        }

        MDC.clear();
        return response;
    }

    private void setErrorSpan() {
        tracer.scopeManager().active().span().setTag("error", true);
    }

}
