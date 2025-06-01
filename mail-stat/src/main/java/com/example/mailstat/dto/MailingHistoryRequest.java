package com.example.mailstat.dto;

import java.time.LocalDateTime;
import java.util.List;

public class MailingHistoryRequest {

    private Long companyId;
    private String subject;
    private String message;
    private String groupName;
    private LocalDateTime sendDate;

    private List<String> emails;

    private List<String> invalidEmails;

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getSubject() {
        return subject;
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

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public List<String> getInvalidEmails() {
        return invalidEmails;
    }

    public void setInvalidEmails(List<String> invalidEmails) {
        this.invalidEmails = invalidEmails;
    }

    // getters / setters ...
}
