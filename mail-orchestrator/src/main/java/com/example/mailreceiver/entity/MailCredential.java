package com.example.mailreceiver.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "mail_credential")
public class MailCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String appPassword;

    @OneToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    public MailCredential() {
    }

    public MailCredential(String email, String appPassword, String smtpHost, Integer smtpPort, Boolean useTls, Company company) {
        this.email = email;
        this.appPassword = appPassword;
        this.company = company;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
    public Company getCompany() {
        return company;
    }
    public void setCompany(Company company) {
        this.company = company;
    }
}
