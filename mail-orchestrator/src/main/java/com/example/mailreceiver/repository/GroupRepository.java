package com.example.mailreceiver.repository;

import com.example.mailreceiver.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<GroupEntity, String> {
    List<GroupEntity> findByCompany_Id(Long companyId);

}