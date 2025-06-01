package com.example.mailintegration.kafka;

import com.example.mailintegration.entity.ActionLog;
import com.example.mailintegration.entity.BusinessProcess;
import com.example.mailintegration.repository.ActionLogRepository;
import com.example.mailintegration.repository.BusinessProcessRepository;
import com.example.mailintegration.repository.BusinessProcessStageRepository;
import dto.StageCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class StageCompletedListener {

    private final BusinessProcessRepository bpRepo;
    private final BusinessProcessStageRepository stageRepo;
    private final ActionLogRepository logRepo;

    private static final String TOPIC = "stage-completed";

    public StageCompletedListener(BusinessProcessRepository bpRepo, BusinessProcessStageRepository stageRepo, ActionLogRepository logRepo) {
        this.bpRepo = bpRepo;
        this.stageRepo = stageRepo;
        this.logRepo = logRepo;
    }

    @KafkaListener(topics = TOPIC, concurrency = "3")
    @Transactional
    public void onStageCompleted(StageCompletedEvent event) {
        log.info("Получено StageCompletedEvent: {}", event);

        BusinessProcess bp = bpRepo.findById(event.bpId())
                .orElseThrow(() -> new IllegalStateException("BP not found: " + event.bpId()));
        BusinessProcessStage stage = stageRepo.findById(event.stageId())
                .orElseThrow(() -> new IllegalStateException("Stage not found: " + event.stageId()));

        ActionLog logEntry = new ActionLog();
        logEntry.setBusinessProcess(bp);
        logEntry.setStage(stage);
        logEntry.setActionDate(event.finishedAt());
        logEntry.setSuccess(event.success());

        logRepo.save(logEntry);

        log.info("Запись журнала создана id={}", logEntry.getId());
    }
}