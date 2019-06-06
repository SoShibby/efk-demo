package com.github.demo.controllers;

import com.github.demo.configuration.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class DemoController {

    private static final Logger log = LoggerFactory.getLogger(DemoController.class);

    private RestTemplate restTemplate;
    private ServerConfig config;

    public DemoController(RestTemplate restTemplate, ServerConfig config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    @RequestMapping("/ping")
    public String ping() {
        log.info("Ping");
        return "Pong";
    }

    @RequestMapping("/chaining")
    public String chaining(HttpSession session) {
        log.info("Chaining Start.");

        String response = sendRequest(HttpMethod.GET, "http://" + config.getHost() + ":" + config.getPort() + "/api/second-chaining", session);

        log.info("Chaining End.");
        return "Chaining. " + response;
    }

    @RequestMapping("/second-chaining")
    public String anotherChaining(HttpSession session) {
        log.info("Second chain start.");

        String response = sendRequest(HttpMethod.GET, "http://" + config.getHost() + ":" + config.getPort() + "/api/third-chaining", session);

        log.info("Second chain end.");
        return "Second chain. " + response;
    }

    @RequestMapping("/third-chaining")
    public String leafChain() {
        log.info("Third chain start.");
        throw new RuntimeException("Throwing an example exception...");
    }

    private String sendRequest(HttpMethod method, String url, HttpSession session)  {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", "JSESSIONID=" + session.getId());
        HttpEntity<String> requestEntity = new HttpEntity<>(null, requestHeaders);
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                method,
                requestEntity,
                String.class);

        return response.getBody();
    }

}
