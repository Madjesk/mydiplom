package com.example.mailintegration.service;

import com.example.mailreceiver.dto.*;
import com.example.mailreceiver.entity.*;
import com.example.mailreceiver.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusinessProcessService {

    private final BusinessProcessRepository bpRepo;
    private final BusinessProcessStageRepository stageRepo;
    private final UserRepository userRepo;

    public BusinessProcessService(BusinessProcessRepository bpRepo,
                                  BusinessProcessStageRepository stageRepo,
                                  UserRepository userRepo) {
        this.bpRepo = bpRepo;
        this.stageRepo = stageRepo;
        this.userRepo = userRepo;
    }

    /*  company() — утилита: по токену вытаскиваем компанию текущего пользователя */
    private Company company(Authentication auth) {
        String email = auth.getName();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getCompany();
    }

    // Создание бизнес-процесса
    public BusinessProcessResponse createBp(BusinessProcessRequest dto,
                                            Authentication auth) {
        BusinessProcess bp = new BusinessProcess();
        bp.setName(dto.name());
        bp.setDescription(dto.description());
        bp.setCompany(company(auth));

        BusinessProcess saved = bpRepo.save(bp);
        return toDto(saved);
    }

    // Список БП для компании
    public List<BusinessProcessResponse> list(Authentication auth) {
        return bpRepo.findByCompany_Id(company(auth).getId())
                .stream()
                .map(this::toDto)
                .toList();
    }

    // Добавить этап
    public BusinessProcessResponse addStage(Long bpId,
                                            BusinessProcessStageRequest dto,
                                            Authentication auth) {
        BusinessProcess bp = bpRepo.findById(bpId)
                .orElseThrow(() -> new RuntimeException("BP not found"));
        // убедимся, что БП принадлежит компании текущего пользователя
        if (!bp.getCompany().getId().equals(company(auth).getId())) {
            throw new RuntimeException("Нет доступа к этому бизнес-процессу");
        }

        BusinessProcessStage st = new BusinessProcessStage();
        st.setName(dto.name());
        st.setGroupName(dto.groupName());
        st.setMailText(dto.mailText());
        st.setPriority(dto.priority());
        st.setBusinessProcess(bp);

        stageRepo.save(st);
        return toDto(bpRepo.findById(bpId).get()); // вернуть обновлённый BP
    }

    /* === mapper === */
    private BusinessProcessResponse toDto(BusinessProcess bp) {
        List<BusinessProcessResponse.StageDto> stages = bp.getStages().stream()
                .map(s -> new BusinessProcessResponse.StageDto(
                        s.getId(), s.getName(), s.getGroupName(),
                        s.getMailText(), s.getPriority()))
                .collect(Collectors.toList());
        return new BusinessProcessResponse(
                bp.getId(), bp.getName(), bp.getDescription(), stages);
    }
}
