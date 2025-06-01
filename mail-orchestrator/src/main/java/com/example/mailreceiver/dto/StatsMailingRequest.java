package com.example.mailreceiver.dto;

import java.time.LocalDateTime;
import java.util.List;

public class StatsMailingRequest {
    private String subject;
    private String message;
    private String groupName;
    private LocalDateTime sendDate;
    private List<String> emails;
    private List<String> invalidEmails;

    public StatsMailingRequest() {
    }

    public StatsMailingRequest(String subject, String message, String groupName,
                               LocalDateTime sendDate,
                               List<String> emails,
                               List<String> invalidEmails) {
        this.subject = subject;
        this.message = message;
        this.groupName = groupName;
        this.sendDate = sendDate;
        this.emails = emails;
        this.invalidEmails = invalidEmails;
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

}
