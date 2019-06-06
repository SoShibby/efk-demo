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
public class AuthFailureHandler implements org.springframework.security.web.authentication.AuthenticationFailureHandler {

    private static Logger log = LoggerFactory.getLogger(AuthFailureHandler.class);

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        String username = request.getParameter("username");
        username = username != null ? username : "";

        log.info(SecurityMarkers.SECURITY_FAILURE, "Invalid username and/or password. Username was '{}'.", username);
        response.sendRedirect("/login");
    }

}
