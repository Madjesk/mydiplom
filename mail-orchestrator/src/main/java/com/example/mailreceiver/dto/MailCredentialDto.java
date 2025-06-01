package com.example.mailreceiver.dto;


public class MailCredentialDto {
    private String email;
    private String appPassword;

    public MailCredentialDto() {
    }

    public MailCredentialDto(String email, String appPassword) {
        this.email = email;
        this.appPassword = appPassword;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getAppPassword() {
        return appPassword;
    }
    public void setAppPassword(String appPassword) {
        this.appPassword = appPassword;
    }
}

