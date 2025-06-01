package com.example.bussinessmailsender.controllers;

import com.example.bussinessmailsender.Service.MailSchedulerService;
import com.example.bussinessmailsender.Service.MailSenderService;
import com.example.bussinessmailsender.dto.GroupEmailRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MailSenderContoller {

    private final MailSenderService mailSenderService;
    private final MailSchedulerService mailSchedulerService;

    public MailSenderContoller(MailSenderService mailSenderService, MailSchedulerService mailSchedulerService) {
        this.mailSenderService = mailSenderService;
        this.mailSchedulerService = mailSchedulerService;
    }

    @PostMapping("/sendmail")
    @ResponseStatus(HttpStatus.OK)
    public String sendMail(@RequestBody GroupEmailRequestDto requestDto) {
        if (requestDto.getEmails() == null || requestDto.getEmails().isEmpty()) {
            return "No emails found for group: " + requestDto.getGroupName();
        }
        for (String email : requestDto.getEmails()) {
            System.out.println(email);
            mailSenderService.sendMail(email, requestDto.getSubject(), requestDto.getMessage());
        }
        return "Sent mail to group: " + requestDto.getGroupName();
    }

    @PostMapping("/scheduleMail")
    public ResponseEntity<String> scheduleMail(@RequestBody GroupEmailRequestDto requestDto) {
        if (requestDto.getEmails() == null || requestDto.getEmails().isEmpty()) {
            return ResponseEntity.ok("No emails found for group: " + requestDto.getGroupName());
        }
        mailSchedulerService.scheduleMail(requestDto);
        return ResponseEntity.ok("Рассылка запланирована: " + requestDto.getSendDate()
                + " " + requestDto.getSendTime());
    }
}