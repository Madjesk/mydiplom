package com.example.mailstat.repository;

import com.example.mailstat.entity.MailingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MailingHistoryRepository extends JpaRepository<MailingHistory, Long> {
    List<MailingHistory> findAllByCompanyId(Long companyId);
    Optional<MailingHistory> findByIdAndCompanyId(Long id, Long companyId);
}