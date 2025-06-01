package com.example.mailreceiver.entity;

import java.util.List;

public class GroupEmailRequest {
    private String groupName;
    private String subject;
    private String message;

    // Если нужно отправлять конкретному списку почт, а не просто groupName:
    private List<String> emails;

    // Новые поля для даты и времени отправки (пока без логики):
    private String sendDate; // формат: yyyy-MM-dd
    private String sendTime; // формат: HH:mm

    public GroupEmailRequest(String groupName, String subject, String message, List<String> emails, String sendDate, String sendTime) {
        this.groupName = groupName;
        this.subject = subject;
        this.message = message;
        this.emails = emails;
        this.sendDate = sendDate;
        this.sendTime = sendTime;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
}
