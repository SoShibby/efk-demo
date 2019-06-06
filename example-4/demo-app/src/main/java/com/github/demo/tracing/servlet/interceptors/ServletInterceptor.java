
package com.github.demo.tracing.servlet.interceptors;

import com.uber.jaeger.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.contrib.spring.web.interceptor.HandlerInterceptorSpanDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ServletInterceptor implements HandlerInterceptor {

    private Tracer tracer;
    private static final Logger log = LoggerFactory.getLogger(ServletInterceptor.class);

    public ServletInterceptor(Tracer tracer, List<HandlerInterceptorSpanDecorator> decorators) {
        this.tracer = tracer;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getUserPrincipal() != null) {
            MDC.put("user-id", request.getUserPrincipal().getName());
        }

        MDC.put("trace-id", getTraceId());
        MDC.put("span-id", getSpanId());
        MDC.put("parent-span-id", getParentSpanId());

        log.info("Incoming request: " + request.getRequestURI());

        response.setHeader("Trace-Id", getTraceId());

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        MDC.put("status-code", String.valueOf(response.getStatus()));
        log.info("Sending response: " + response.getStatus() + " for request " + request.getRequestURI());
        MDC.clear();
    }

    private String getTraceId() {
        return String.format("%x", getSpanContext().getTraceId());
    }

    private String getSpanId() {
        return String.format("%x", getSpanContext().getSpanId());
    }

    private String getParentSpanId() {
        return String.format("%x", getSpanContext().getParentId());
    }

    private SpanContext getSpanContext() {
        return (SpanContext) tracer.scopeManager().active().span().context();
    }
}
