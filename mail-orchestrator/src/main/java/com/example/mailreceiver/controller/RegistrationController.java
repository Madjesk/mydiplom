package com.example.mailreceiver.controller;

import com.example.mailreceiver.service.UserService;
import com.example.mailreceiver.service.ValidationService;
import com.example.mailreceiver.exceptions.BusinessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {
    private final UserService userService;
    private final ValidationService validationService;

    public RegistrationController(UserService userService, ValidationService validationService) {
        this.userService = userService;
        this.validationService = validationService;
    }

    @PostMapping("/register-company")
    public String registerCompany(@RequestParam String newCompanyName,
                                  @RequestParam String newCompanyEmail,
                                  @RequestParam String newCompanyPassword) {
        try {
            validationService.validateCompanyRegistration(newCompanyName, newCompanyEmail);
            userService.registerNewCompany(newCompanyName, newCompanyEmail, newCompanyPassword);
            return "redirect:/login?registrationSuccess=true";
        } catch (BusinessException e) {
            return redirectWithError(e.getMessage());
        }
    }

    @PostMapping("/join-company")
    public String joinCompany(@RequestParam String joinCompanyName,
                              @RequestParam String joinEmail,
                              @RequestParam String joinPassword) {
        try {
            validationService.validateUserRegistration(joinCompanyName, joinEmail);
            userService.joinExistingCompany(joinCompanyName, joinEmail, joinPassword);
            return "redirect:/login?waitForApproval=true";
        } catch (BusinessException e) {
            return redirectWithError(e.getMessage());
        }
    }

    private String redirectWithError(String message) {
        String errorParam = switch (message) {
            case "Company already exists" -> "companyExists";
            case "Company not found" -> "companyNotFound";
            case "Email already registered" -> "emailExists";
            default -> "registrationError";
        };
        return "redirect:/login?" + errorParam + "=true";
    }
}