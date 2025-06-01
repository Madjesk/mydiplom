package com.example.mailreceiver.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "contacts")
public class ContactEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contactName;
    private String email;

    @ManyToOne
    @JoinColumn(name = "group_name")
    private GroupEntity group;

    public ContactEntity() {
    }

    public ContactEntity(String contactName, String email, GroupEntity group) {
        this.contactName = contactName;
        this.email = email;
        this.group = group;
    }

    // getters/setters
    public Long getId() {
        return id;
    }

    public String getContactName() {
        return contactName;
    }
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public GroupEntity getGroup() {
        return group;
    }
    public void setGroup(GroupEntity group) {
        this.group = group;
    }
}