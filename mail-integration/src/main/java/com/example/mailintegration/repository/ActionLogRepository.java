package com.example.mailintegration.repository;

import com.example.mailintegration.entity.ActionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {
    List<ActionLog> findByBusinessProcess_Company_Id(Long companyId);
}
