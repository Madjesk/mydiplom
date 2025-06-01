package com.example.mailreceiver.exceptions;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        String errorParam = "error";

        if (exception instanceof DisabledException) {
            errorParam = "accountNotApproved";
        } else if (exception instanceof BadCredentialsException) {
            errorParam = "error";
        }

        setDefaultFailureUrl("/login?" + errorParam);
        super.onAuthenticationFailure(request, response, exception);
    }
}
