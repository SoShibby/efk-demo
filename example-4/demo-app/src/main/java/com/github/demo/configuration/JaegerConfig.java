package com.github.demo.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;


@Configuration
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "jaeger")
@Validated
public class JaegerConfig {

    @NotBlank
    private String url;

    @NotBlank
    private String serviceName;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
