package com.example.mailintegration.entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "action_log")
public class ActionLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private BusinessProcess businessProcess;

    @ManyToOne(fetch = FetchType.LAZY)
    private BusinessProcessStage stage;

    private LocalDateTime actionDate;

    @Column(nullable = false)
    private boolean success;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BusinessProcess getBusinessProcess() {
        return businessProcess;
    }

    public void setBusinessProcess(BusinessProcess businessProcess) {
        this.businessProcess = businessProcess;
    }

    public BusinessProcessStage getStage() {
        return stage;
    }

    public void setStage(BusinessProcessStage stage) {
        this.stage = stage;
    }

    public LocalDateTime getActionDate() {
        return actionDate;
    }

    public void setActionDate(LocalDateTime actionDate) {
        this.actionDate = actionDate;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}
