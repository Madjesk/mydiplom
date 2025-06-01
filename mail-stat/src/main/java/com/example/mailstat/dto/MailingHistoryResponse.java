package com.example.mailstat.dto;

import java.time.LocalDateTime;
import java.util.List;

public class MailingHistoryResponse {
    private Long mailingId;
    private String subject;
    private String message;
    private String groupName;
    private LocalDateTime sendDate;
    private int totalRecipients;
    private int failedRecipientsCount;
    private Long companyId;

    private List<RecipientDto> recipients;

    public Long getMailingId() {
        return mailingId;
    }

    public void setMailingId(Long mailingId) {
        this.mailingId = mailingId;
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

    public List<RecipientDto> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<RecipientDto> recipients) {
        this.recipients = recipients;
    }

    // getters / setters ...

    public static class RecipientDto {
        private String email;
        private boolean delivered;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public boolean isDelivered() {
            return delivered;
        }

        public void setDelivered(boolean delivered) {
            this.delivered = delivered;
        }
        // getters / setters ...
    }
}
