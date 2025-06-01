package com.example.mailreceiver.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name", unique = true)
    private String companyName;

    // Связь One-to-One, т.к. для компании может быть только один набор настроек
    @OneToOne(mappedBy = "company", cascade = CascadeType.ALL)
    private MailCredential mailCredential;

    public Company() {
    }

    public Company(String companyName) {
        this.companyName = companyName;
    }

    // Getters и setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public MailCredential getMailCredential() {
        return mailCredential;
    }
    public void setMailCredential(MailCredential mailCredential) {
        this.mailCredential = mailCredential;
    }
}
