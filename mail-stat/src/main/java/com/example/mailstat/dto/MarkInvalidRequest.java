package com.example.mailstat.dto;

public class MarkInvalidRequest {
    private Long mailingId;
    private String email;

    public Long getMailingId() {
        return mailingId;
    }

    public void setMailingId(Long mailingId) {
        this.mailingId = mailingId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}