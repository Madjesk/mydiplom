package com.example.mailstat.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mailing_history")
public class MailingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;
    private String message;
    private String groupName;
    private LocalDateTime sendDate;

    private int totalRecipients;
    private int failedRecipientsCount;
    @Column(name="company_id", nullable = false)
    private Long companyId;

    @OneToMany(mappedBy = "mailingHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MailingRecipient> recipients = new ArrayList<>();


    public void incrementFailedCount() {
        this.failedRecipientsCount++;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setCompanyId(Long id) {
        this.companyId = id;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public LocalDateTime getSendDate() {
        return sendDate;
    }

    public void setSendDate(LocalDateTime sendDate) {
        this.sendDate = sendDate;
    }

    public int getTotalRecipients() {
        return totalRecipients;
    }

    public void setTotalRecipients(int totalRecipients) {
        this.totalRecipients = totalRecipients;
    }

    public int getFailedRecipientsCount() {
        return failedRecipientsCount;
    }

    public void setFailedRecipientsCount(int failedRecipientsCount) {
        this.failedRecipientsCount = failedRecipientsCount;
    }

    public List<MailingRecipient> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<MailingRecipient> recipients) {
        this.recipients = recipients;
    }

    }

