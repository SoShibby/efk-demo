package com.github.demo.tracing;

import com.uber.jaeger.Tracer;
import com.uber.jaeger.reporters.RemoteReporter;
import com.uber.jaeger.samplers.ConstSampler;
import com.uber.jaeger.senders.HttpSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.demo.configuration.JaegerConfig;

@Configuration
public class JaegerTracerConfig {

    private JaegerConfig config;

    public JaegerTracerConfig(JaegerConfig config) {
        this.config = config;
    }

    @Bean
    public Tracer jaegerTracer() {
        return new Tracer.Builder(config.getServiceName())
                .withReporter(new RemoteReporter.Builder().withSender(new HttpSender.Builder(config.getUrl() + "/api/traces").build()).build())
                .withSampler(new ConstSampler(true))
                .build();
    }
}
