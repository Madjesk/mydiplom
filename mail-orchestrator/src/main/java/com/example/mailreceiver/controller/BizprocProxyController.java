package com.example.mailreceiver.controller;

import com.example.mailreceiver.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BizprocProxyController {

    private final RestTemplate rest;
    private final UserService  userService;
    private static final String BIZ_URL = "http://localhost:8083/api/bizprocs";

    @GetMapping("/bizprocs")
    public ResponseEntity<?> listBizprocs(HttpServletRequest req) {
        return rest.exchange(
                BIZ_URL + "/bizprocs",
                HttpMethod.GET,
                withJwt(null, extractJwt(req)),
                Object.class
        );
    }

    @PostMapping("/bizprocs")
    public ResponseEntity<?> createBizproc(@RequestBody String body,
                                           HttpServletRequest req) {
        return rest.exchange(
                BIZ_URL + "/bizprocs",
                HttpMethod.POST,
                withJwt(body, extractJwt(req)),
                Object.class
        );
    }

    @PostMapping("/bizprocs/{bpId}/stages")
    public ResponseEntity<?> addStage(@PathVariable Long bpId,
                                      @RequestBody String body,
                                      HttpServletRequest req) {
        return rest.exchange(
                BIZ_URL + "/bizprocs/" + bpId + "/stages",
                HttpMethod.POST,
                withJwt(body, extractJwt(req)),
                Object.class
        );
    }

    @GetMapping("/actions")
    public ResponseEntity<?> listActions(HttpServletRequest req) {
        return rest.exchange(
                BIZ_URL + "/actions",
                HttpMethod.GET,
                withJwt(null, extractJwt(req)),
                Object.class
        );
    }

    @PostMapping("/actions")
    public ResponseEntity<?> performAction(@RequestBody String body,
                                           HttpServletRequest req) {
        return rest.exchange(
                BIZ_URL + "/actions",
                HttpMethod.POST,
                withJwt(body, extractJwt(req)),
                Object.class
        );
    }


    private HttpEntity<?> withJwt(Object body, String jwt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt);
        if (body != null) headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    private String extractJwt(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie c : request.getCookies()) {
            if ("JWT-COOKIE".equals(c.getName())) {
                return c.getValue();
            }
        }
        return null;
    }
}