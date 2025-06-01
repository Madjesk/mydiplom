package com.example.mailreceiver.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groups")
public class GroupEntity {

    @Id
    private String groupName;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<ContactEntity> contacts = new ArrayList<>();

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    public GroupEntity() {
    }

    public GroupEntity(String groupName, Company company) {
        this.groupName = groupName;
        this.company = company;
    }

    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<ContactEntity> getContacts() {
        return contacts;
    }
    public void setContacts(List<ContactEntity> contacts) {
        this.contacts = contacts;
    }
}
