package com.example.mailintegration.controller;

import com.example.mailintegration.service.BusinessProcessService;
import dto.BusinessProcessRequest;
import dto.BusinessProcessResponse;
import dto.BusinessProcessStageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bizprocs")
public class BusinessProcessController {

    private final BusinessProcessService service;

    public BusinessProcessController(BusinessProcessService service) {
        this.service = service;
    }

    @GetMapping
    public List<BusinessProcessResponse> list(Authentication auth) {
        return service.list(auth);
    }

    @PostMapping
    public ResponseEntity<BusinessProcessResponse>
    create(@RequestBody BusinessProcessRequest dto,
           Authentication auth) {
        return ResponseEntity.ok(service.createBp(dto, auth));
    }

    @PostMapping("/{bpId}/stages")
    public ResponseEntity<BusinessProcessResponse>
    addStage(@PathVariable Long bpId,
             @RequestBody BusinessProcessStageRequest dto,
             Authentication auth) {
        return ResponseEntity.ok(service.addStage(bpId, dto, auth));
    }
}
