package com.example.mailreceiver.controller;

import com.example.mailreceiver.service.JwtService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public String doLogin(LoginRequest loginRequest,
                          HttpServletResponse response) {
        try {
            // Аутентифицируем (проверяем логин/пароль)
            var authToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getLoginEmail(),
                    loginRequest.getLoginPassword()
            );
            authenticationManager.authenticate(authToken);

            // Если сюда дошли – логин/пароль валидны. Генерим JWT
            String jwt = jwtService.generateToken(loginRequest.getLoginEmail());

            // Кладём JWT в HttpOnly-cookie
            Cookie cookie = new Cookie("JWT-COOKIE", jwt);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            return "redirect:/inbox";
        } catch (AuthenticationException ex) {
            return "redirect:/login?error=true";
        }
    }

    public static class LoginRequest {
        private String loginEmail;
        private String loginPassword;

        public LoginRequest(String loginEmail, String loginPassword) {
            this.loginEmail = loginEmail;
            this.loginPassword = loginPassword;
        }


        public String getLoginEmail() {
            return loginEmail;
        }

        public void setLoginEmail(String loginEmail) {
            this.loginEmail = loginEmail;
        }

        public String getLoginPassword() {
            return loginPassword;
        }

        public void setLoginPassword(String loginPassword) {
            this.loginPassword = loginPassword;
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("JWT-COOKIE", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/login?logout=true";
    }

}
