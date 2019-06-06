package com.github.demo.exceptionmapper;

public class ApiError {
    private String traceId;
    private String spanId;
    private String message;

    public ApiError() {

    }

    public ApiError(String traceId, String spanId, String message) {
        this.traceId = traceId;
        this.spanId = spanId;
        this.message = message;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getSpanId() {
        return spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
