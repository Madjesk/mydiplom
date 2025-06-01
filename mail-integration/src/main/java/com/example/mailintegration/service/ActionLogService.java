package com.example.mailintegration.service;
import com.example.mailreceiver.dto.*;
import com.example.mailreceiver.entity.*;
import com.example.mailreceiver.repository.*;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActionLogService {

    private final BusinessProcessRepository bpRepo;
    private final BusinessProcessStageRepository stageRepo;
    private final ActionLogRepository logRepo;
    private final UserRepository userRepo;
    private final ContactRepository contactRepo;
    private final RestTemplate rest;
    private final String senderServiceUrl = "http://localhost:8080";
    private final String statsServiceUrl  = "http://localhost:8082/api/stats";

    public ActionLogService(BusinessProcessRepository bpRepo,
                            BusinessProcessStageRepository stageRepo,
                            ActionLogRepository logRepo,
                            UserRepository userRepo,
                            ContactRepository contactRepo,
                            RestTemplateBuilder builder) {
        this.bpRepo      = bpRepo;
        this.stageRepo   = stageRepo;
        this.logRepo     = logRepo;
        this.userRepo    = userRepo;
        this.contactRepo = contactRepo;
        this.rest        = builder.build();
    }

    private Company company(Authentication auth) {
        return userRepo.findByEmail(auth.getName()).orElseThrow().getCompany();
    }

    private List<String> emailsByGroup(String groupName) {
        return contactRepo.findByGroup_GroupName(groupName)
                .stream()
                .map(ContactEntity::getEmail)
                .collect(Collectors.toList());
    }

    @Transactional
    public ActionLogResponse perform(ActionRequest req, Authentication auth) {

        BusinessProcess bp = bpRepo.findById(req.bpId()).orElseThrow();
        if (!bp.getCompany().getId().equals(company(auth).getId()))
            throw new RuntimeException("Нет доступа к бизнес-процессу");

        BusinessProcessStage stage = stageRepo.findById(req.stageId()).orElseThrow();

        GroupEmailRequestDto dto = new GroupEmailRequestDto();
        dto.setGroupName(stage.getGroupName());
        dto.setSubject(bp.getName() + " – " + stage.getName());
        dto.setMessage(stage.getMailText());
        dto.setEmails(emailsByGroup(stage.getGroupName()));   // <-- заполняем email-ы
        dto.setSendDate(null);
        dto.setSendTime(null);

        boolean ok;
        try {
            HttpHeaders hdr = new HttpHeaders();
            hdr.setContentType(MediaType.APPLICATION_JSON);
            rest.postForEntity(senderServiceUrl + "/sendmail",
                    new HttpEntity<>(dto, hdr),
                    String.class);
            ok = true;

            try {

                StatsMailingRequest statsReq = new StatsMailingRequest(
                        dto.getSubject(),
                        dto.getMessage(),
                        dto.getGroupName(),
                        LocalDateTime.now(),
                        dto.getEmails(),
                        List.of()
                );
                rest.postForEntity(statsServiceUrl + "/logMailing",
                        statsReq,
                        StatsMailingResponse.class);
            } catch (Exception ignore) { /* не критично */ }

        } catch (Exception e) {
            ok = false;
        }

        ActionLog log = new ActionLog();
        log.setBusinessProcess(bp);
        log.setStage(stage);
        log.setActionDate(LocalDateTime.now());
        log.setSuccess(ok);
        logRepo.save(log);

        return new ActionLogResponse(
                bp.getName(),
                stage.getName(),
                log.getActionDate().toString(),
                ok);
    }

    @Transactional(readOnly = true)
    public List<ActionLogResponse> list(Authentication auth) {
        return logRepo.findByBusinessProcess_Company_Id(company(auth).getId())
                .stream()
                .map(l -> new ActionLogResponse(
                        l.getBusinessProcess().getName(),
                        l.getStage().getName(),
                        l.getActionDate().toString(),
                        l.isSuccess()))
                .toList();
    }
}
