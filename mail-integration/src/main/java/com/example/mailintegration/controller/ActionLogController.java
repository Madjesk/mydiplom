package com.example.mailintegration.controller;

import com.example.mailintegration.service.ActionLogService;
import dto.ActionLogResponse;
import dto.ActionRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actions")
public class ActionLogController {

    private final ActionLogService service;

    public ActionLogController(ActionLogService service) { this.service = service; }

    @GetMapping
    public List<ActionLogResponse> list(Authentication auth) {
        return service.list(auth);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ActionLogResponse perform(@RequestBody ActionRequest req, Authentication auth) {
        return service.perform(req, auth);
    }
}
