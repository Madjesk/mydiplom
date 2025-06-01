package com.example.mailreceiver.controller;

import com.example.mailreceiver.service.UserService;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ProxyController {
    private RestTemplate restTemplate = new RestTemplate();
    private static final String statsBase = "http://localhost:8082/api/stats";
    private final UserService userService;

    public ProxyController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getAllStats() {
        String url = "http://localhost:8082/api/stats";
        return restTemplate.exchange(url, HttpMethod.GET, null, Object.class);
    }

    @GetMapping("/stats/{id}")
    public ResponseEntity<?> getStats(@PathVariable Long id) {
        System.out.println("WANT STAT- "+ id);
        String url = "http://localhost:8082/api/stats/" + id;
        return restTemplate.exchange(url, HttpMethod.GET, null, Object.class);
    }
}

