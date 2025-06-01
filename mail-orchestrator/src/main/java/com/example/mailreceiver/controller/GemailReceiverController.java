package com.example.mailreceiver.controller;

import com.example.mailreceiver.dto.StatsMailingRequest;
import com.example.mailreceiver.dto.StatsMailingResponse;
import com.example.mailreceiver.entity.Company;
import com.example.mailreceiver.entity.ContactEntity;
import com.example.mailreceiver.entity.MailCredential;
import com.example.mailreceiver.entity.User;
import com.example.mailreceiver.service.UserService;
import com.example.mailreceiver.repository.ContactRepository;
import com.example.mailreceiver.dto.GroupEmailRequestDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class GemailReceiverController {

    private final RestTemplate restTemplate;
    private final ContactRepository contactRepository;
    private final UserService userService;

    private final String senderServiceUrl = "http://localhost:8080";

    private final String statsServiceUrl = "http://localhost:8082/api/stats";

    public GemailReceiverController(RestTemplateBuilder builder,
                                    ContactRepository contactRepository,
                                    UserService userService) {
        this.restTemplate = builder.build();
        this.contactRepository = contactRepository;
        this.userService = userService;
    }

    @GetMapping("/inbox")
    public String inbox(Model model, Authentication authentication) {
        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            User admin = userService.findByEmail(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Admin not found"));
            List<User> pendingUsers = userService.getPendingUsers(admin.getCompany().getId());
            model.addAttribute("pendingUsers", pendingUsers);
        }
        return "inbox";
    }

    @PostMapping("/sendmail")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> sendMail(@RequestBody GroupEmailRequestDto requestDto) {
        try {
            if (requestDto.getGroupName() != null && !requestDto.getGroupName().isEmpty()) {
                List<ContactEntity> contacts = contactRepository.findByGroup_GroupName(requestDto.getGroupName());
                List<String> emails = contacts.stream()
                        .map(ContactEntity::getEmail)
                        .collect(Collectors.toList());
                requestDto.setEmails(emails);
            }

            boolean schedule = isFutureDate(requestDto.getSendDate(), requestDto.getSendTime());
            String targetUrl = schedule ? (senderServiceUrl + "/scheduleMail")
                    : (senderServiceUrl + "/sendmail");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<GroupEmailRequestDto> httpEntity = new HttpEntity<>(requestDto, headers);

            ResponseEntity<String> senderResponse = restTemplate.postForEntity(targetUrl, httpEntity, String.class);

            if (senderResponse.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Ошибка при отправке через BusinessMailSender: "
                                + senderResponse.getStatusCodeValue());
            }

            try {
                StatsMailingRequest statsRequest = new StatsMailingRequest(
                        requestDto.getSubject(),
                        requestDto.getMessage(),
                        requestDto.getGroupName(),
                        LocalDateTime.now(),
                        requestDto.getEmails(),
                        List.of()
                );
                ResponseEntity<StatsMailingResponse> statsResp = restTemplate.postForEntity(
                        statsServiceUrl + "/logMailing",
                        statsRequest,
                        StatsMailingResponse.class
                );
                System.out.println("Stats saved with ID: " + statsResp.getBody().getMailingId());
            } catch (Exception e) {
                System.err.println("Failed to log mailing in Stats service: " + e.getMessage());
            }

            return ResponseEntity.ok(schedule ? "Рассылка запланирована" : "Рассылка отправлена");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Сервис рассылки недоступен или ошибка: " + e.getMessage());
        }
    }

    private boolean isFutureDate(String sendDate, String sendTime) {
        if (sendDate == null || sendDate.isEmpty()) return false;
        try {
            LocalDate date = LocalDate.parse(sendDate);
            LocalTime time = (sendTime == null || sendTime.isEmpty())
                    ? LocalTime.MIDNIGHT
                    : LocalTime.parse(sendTime);
            LocalDateTime dt = LocalDateTime.of(date, time);

            return dt.isAfter(LocalDateTime.now().plusMinutes(1));
        } catch (Exception e) {
            return false;
        }
    }

}