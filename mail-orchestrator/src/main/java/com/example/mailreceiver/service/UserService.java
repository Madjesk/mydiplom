package com.example.mailreceiver.service;

import com.example.mailreceiver.entity.Company;
import com.example.mailreceiver.entity.Role;
import com.example.mailreceiver.repository.CompanyRepository;
import com.example.mailreceiver.repository.UserRepository;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import com.example.mailreceiver.entity.User;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       CompanyRepository companyRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerNewCompany(String companyName, String email, String rawPassword) {
        Company company = new Company();
        company.setCompanyName(companyName);
        company = companyRepository.save(company);

        User user = new User();
        user.setEmail(email);
        String encoded = passwordEncoder.encode(rawPassword);
        if (encoded == null || encoded.isEmpty()) {
            throw new IllegalStateException("Password encoding failed!");
        }
        user.setPassword(encoded);
        user.setRole(Role.ADMIN);
        user.setEnabled(true);
        user.setCompany(company);

        userRepository.save(user);
    }

    public void joinExistingCompany(String companyName, String email, String rawPassword) {
        Optional<Company> companyOpt = companyRepository.findByCompanyName(companyName);
        if (companyOpt.isEmpty()) {
            throw new RuntimeException("Компания не найдена!");
        }

        Company company = companyOpt.get();
        User user = new User();
        user.setEmail(email);

        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(Role.USER);
        user.setEnabled(false);
        LocalDate date = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        user.setRegistrationDate(date.format(formatter));
        user.setCompany(company);

        userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void approveUser(Long userId, String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Администратор не найден"));

        if (admin.getRole() != Role.ADMIN) {
            throw new RuntimeException("Вы не администратор!");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (!user.getCompany().getId().equals(admin.getCompany().getId())) {
            throw new RuntimeException("Нельзя одобрять пользователей другой компании!");
        }

        user.setEnabled(true);
        userRepository.save(user);
    }

    public void toggleUserStatus(Long userId, boolean enabled, String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Администратор не найден"));
        if (admin.getRole() != Role.ADMIN) {
            throw new RuntimeException("Вы не администратор!");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (!user.getCompany().getId().equals(admin.getCompany().getId())) {
            throw new RuntimeException("Нельзя изменять статус пользователей другой компании!");
        }

        user.setEnabled(enabled);
        userRepository.save(user);
    }


    public List<User> getPendingUsers(Long companyId) {
        return userRepository.findByCompanyId(companyId);
    }

    public void changeUserRole(Long userId, String newRoleString, String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Администратор не найден"));

        if (admin.getRole() != Role.ADMIN) {
            throw new RuntimeException("Вы не администратор!");
        }

        Role newRole;
        try {
            newRole = Role.valueOf(newRoleString);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Неверная роль: " + newRoleString);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (!user.getCompany().getId().equals(admin.getCompany().getId())) {
            throw new RuntimeException("Нельзя менять роль пользователям другой компании!");
        }

        user.setRole(newRole);
        userRepository.save(user);
    }

}
