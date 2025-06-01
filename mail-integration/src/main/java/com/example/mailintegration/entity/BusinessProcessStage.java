package com.example.mailintegration.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "business_process_stage")
public class BusinessProcessStage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;            // «Название этапа»

    @Column(nullable = false)
    private String groupName;       // Группа рассылки (строкой)

    @Column(columnDefinition = "text", nullable = false)
    private String mailText;      // Текст письма

    private int priority;           // Приоритет

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bp_id", nullable = false)
    private BusinessProcess businessProcess;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getMailText() {
        return mailText;
    }

    public void setMailText(String mailText) {
        this.mailText = mailText;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public BusinessProcess getBusinessProcess() {
        return businessProcess;
    }

    public void setBusinessProcess(BusinessProcess businessProcess) {
        this.businessProcess = businessProcess;
    }

    /* геттеры/сеттеры */
}
