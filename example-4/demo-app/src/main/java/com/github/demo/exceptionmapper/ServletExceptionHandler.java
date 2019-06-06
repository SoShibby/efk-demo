package com.github.demo.exceptionmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uber.jaeger.SpanContext;
import io.opentracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@ControllerAdvice
@RestController
public class ServletExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ServletExceptionHandler.class);
    private Tracer tracer;
    private ObjectMapper mapper = new ObjectMapper();

    public ServletExceptionHandler(Tracer tracer) {
        this.tracer = tracer;
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ApiError> handleAllExceptions(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        ApiError response = parseException(ex);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Exception-Span", response.getSpanId());

        tracer.scopeManager().active().span().setTag("error", true);

        return new ResponseEntity<>(response, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ApiError parseException(Exception ex) {
        if (ex instanceof HttpServerErrorException) {
            return parseHttpResponseException((HttpServerErrorException) ex);
        } else {
            return parseGeneralException(ex);
        }
    }

    private ApiError parseHttpResponseException(HttpServerErrorException ex) {
        String body = ex.getResponseBodyAsString();

        try {
            return mapper.readValue(body, ApiError.class);
        } catch (IOException e) {
            return new ApiError(getTraceId(), getSpanId(), body);
        }
    }

    private ApiError parseGeneralException(Exception ex) {
        return new ApiError(getTraceId(), getSpanId(), ex.getMessage());
    }

    private String getTraceId() {
        SpanContext context = (SpanContext) tracer.scopeManager().active().span().context();
        return String.format("%x", context.getTraceId());
    }

    private String getSpanId() {
        SpanContext context = (SpanContext) tracer.scopeManager().active().span().context();
        return String.format("%x", context.getSpanId());
    }

}
