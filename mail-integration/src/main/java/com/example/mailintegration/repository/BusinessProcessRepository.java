package com.example.mailintegration.repository;

import com.example.mailintegration.entity.BusinessProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BusinessProcessRepository extends JpaRepository<BusinessProcess, Long> {
    List<BusinessProcess> findByCompany_Id(Long companyId);
}
