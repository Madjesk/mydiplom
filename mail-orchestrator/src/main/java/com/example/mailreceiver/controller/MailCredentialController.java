package com.example.mailreceiver.controller;

import com.example.mailreceiver.dto.MailCredentialDto;
import com.example.mailreceiver.entity.Company;
import com.example.mailreceiver.entity.MailCredential;
import com.example.mailreceiver.entity.User;
import com.example.mailreceiver.repository.MailCredentialRepository;
import com.example.mailreceiver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class MailCredentialController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailCredentialRepository mailCredentialRepository;

    @PostMapping("/saveCredentials")
    public ResponseEntity<String> saveCredentials(@ModelAttribute MailCredentialDto dto, Authentication authentication) {
        try {
            String userEmail = authentication.getName();

            Optional<User> optionalUser = userRepository.findByEmail(userEmail);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(401).body("Пользователь не найден");
            }
            User user = optionalUser.get();

            if (user.getCompany() == null) {
                return ResponseEntity.badRequest().body("Компания не привязана к пользователю");
            }

            MailCredential credential = new MailCredential();
            credential.setEmail(dto.getEmail());
            credential.setAppPassword(dto.getAppPassword());

            Company company = user.getCompany();
            credential.setCompany(company);
            company.setMailCredential(credential);

            mailCredentialRepository.save(credential);

            return ResponseEntity.ok("Данные сохранены");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при сохранении данных: " + e.getMessage());
        }
    }

}
