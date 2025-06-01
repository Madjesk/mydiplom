package com.example.mailreceiver.repository;

import com.example.mailreceiver.entity.GroupEntity;
import com.example.mailreceiver.entity.MailCredential;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailCredentialRepository extends JpaRepository<MailCredential, Long> {
}
