package com.example.mailreceiver.repository;

import com.example.mailreceiver.entity.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<ContactEntity, Long> {
    List<ContactEntity> findByGroup_GroupName(String groupName);
}