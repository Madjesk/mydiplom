package com.example.mailreceiver.controller;

import com.example.mailreceiver.dto.GroupShortDto;
import com.example.mailreceiver.entity.Company;
import com.example.mailreceiver.entity.ContactEntity;
import com.example.mailreceiver.entity.User;
import com.example.mailreceiver.service.UserService;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.mailreceiver.entity.GroupEntity;
import com.example.mailreceiver.repository.ContactRepository;
import com.example.mailreceiver.repository.GroupRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Controller
public class GroupController {

    private final GroupRepository groupRepository;
    private final ContactRepository contactRepository;
    private final UserService userService;

    public GroupController(GroupRepository groupRepository, ContactRepository contactRepository, UserService userService) {
        this.groupRepository = groupRepository;
        this.contactRepository = contactRepository;
        this.userService = userService;
    }

    @PostMapping("/groups/create")
    @ResponseBody
    public ResponseEntity<String> createGroup(
            @RequestParam("groupName") String groupName,
            @RequestParam("file") MultipartFile file, Authentication authentication
    ) {
        try {
            Company company = userService.findByEmail(authentication.getName())
                    .orElseThrow().getCompany();

            GroupEntity group = new GroupEntity();
            group.setGroupName(groupName);
            group.setCompany(company);
            groupRepository.save(group);

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length == 2) {
                        String name = parts[0].trim();
                        String email = parts[1].trim();
                        ContactEntity contact = new ContactEntity();
                        contact.setContactName(name);
                        contact.setEmail(email);
                        contact.setGroup(group);
                        contactRepository.save(contact);
                    }
                }
            }

            return ResponseEntity.ok("Группа успешно создана");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при создании группы: " + e.getMessage());
        }
    }

    @GetMapping("/groups")
    @ResponseBody
    public List<GroupShortDto> listGroups(Authentication authentication) {
        Long companyId = userService.findByEmail(authentication.getName())
                .orElseThrow().getCompany().getId();
        return groupRepository.findByCompany_Id(companyId)
                .stream()
                .map(g -> new GroupShortDto(g.getGroupName(),
                        g.getContacts().size()))
                .toList();
    }

    @DeleteMapping("/groups/{name}")
    @ResponseBody
    public ResponseEntity<?> deleteGroup(@PathVariable String name,
                                         Authentication authentication) {
        User user = userService.findByEmail(authentication.getName())
                .orElseThrow();
        Optional<GroupEntity> optGroup = groupRepository.findById(name);
        if (optGroup.isEmpty())
            return ResponseEntity.notFound().build();

        GroupEntity group = optGroup.get();

        if (!group.getCompany().getId().equals(user.getCompany().getId()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Чужая группа");

        contactRepository.deleteAll(group.getContacts());

        groupRepository.delete(group);
        return ResponseEntity.ok().build();
    }
}