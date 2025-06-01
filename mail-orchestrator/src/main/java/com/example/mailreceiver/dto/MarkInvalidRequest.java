package com.example.mailreceiver.dto;

public class MarkInvalidRequest {
    private Long mailingId;
    private String email;

    public MarkInvalidRequest() {}
    public MarkInvalidRequest(Long mailingId, String email) {
        this.mailingId = mailingId;
        this.email = email;
    }

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
