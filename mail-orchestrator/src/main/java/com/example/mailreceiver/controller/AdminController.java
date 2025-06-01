package com.example.mailreceiver.controller;

import com.example.mailreceiver.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/admin/approve-user")
    @ResponseBody
    public String approveUser(@RequestParam Long userId, Authentication authentication) {
        String adminEmail = authentication.getName();
        userService.approveUser(userId, adminEmail);
        return "Пользователь одобрен";
    }

    @PostMapping("/admin/toggle-user")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public String toggleUserStatus(@RequestParam Long userId,
                                   @RequestParam boolean enabled,
                                   Authentication authentication) {
        String adminEmail = authentication.getName();
        userService.toggleUserStatus(userId, enabled, adminEmail);
        return enabled ? "Пользователь одобрен" : "Пользователь деактивирован";
    }

    @PostMapping("/admin/change-role")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public String changeUserRole(@RequestParam Long userId,
                                 @RequestParam String newRole,
                                 Authentication authentication) {
        String adminEmail = authentication.getName();
        userService.changeUserRole(userId, newRole, adminEmail);
        return "Роль успешно изменена на " + newRole;
    }


}
