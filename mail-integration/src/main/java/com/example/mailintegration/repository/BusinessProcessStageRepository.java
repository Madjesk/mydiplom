package com.example.mailintegration.repository;

import com.example.mailintegration.entity.BusinessProcessStage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BusinessProcessStageRepository extends JpaRepository<BusinessProcessStage, Long> {
    List<BusinessProcessStage> findByBusinessProcess_Id(Long bpId);
}
