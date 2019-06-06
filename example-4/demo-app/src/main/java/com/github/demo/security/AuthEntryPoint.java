package com.github.demo.security;

import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {

    private static Logger log = LoggerFactory.getLogger(AuthEntryPoint.class.getName());

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        log.info(SecurityMarkers.SECURITY_AUDIT, "Anonymous account access. Forwarding to login. For path '{}'.", request.getRequestURI());
        response.sendRedirect("/login");
    }

}
