package com.example.mailreceiver.service;

import com.example.mailreceiver.exceptions.BusinessException;
import com.example.mailreceiver.repository.CompanyRepository;
import com.example.mailreceiver.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public ValidationService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public void validateCompanyRegistration(String companyName, String email) {
        if (companyRepository.existsByCompanyName(companyName)) {
            throw new BusinessException("Company already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException("Email already registered");
        }
    }

    public void validateUserRegistration(String companyName, String email) {
        if (!companyRepository.existsByCompanyName(companyName)) {
            throw new BusinessException("Company not found");
        }

        if (userRepository.existsByEmail(email)) {
            throw new BusinessException("Email already registered");
        }
    }
}
