package com.example.mailstat.controller;

import com.example.mailstat.StatsService;
import com.example.mailstat.dto.MailingHistoryRequest;
import com.example.mailstat.dto.MailingHistoryResponse;
import com.example.mailstat.dto.MarkInvalidRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @PostMapping("/logMailing")
    public ResponseEntity<MailingHistoryResponse> logMailing(
            @RequestBody MailingHistoryRequest req) {
        return ResponseEntity.ok(statsService.saveMailing(req));
    }

    @GetMapping
    public List<MailingHistoryResponse> getAll(@RequestParam Long companyId) {
        return statsService.getAllMailings(companyId);
    }

    @PostMapping("/markInvalid")
    public ResponseEntity<Void> markInvalid(@RequestBody MarkInvalidRequest request) {
        statsService.markInvalid(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MailingHistoryResponse> getOne(
            @PathVariable Long id,
            @RequestParam Long companyId) {
        MailingHistoryResponse r = statsService.getMailing(id, companyId);
        return r != null ? ResponseEntity.ok(r) : ResponseEntity.notFound().build();
    }
}