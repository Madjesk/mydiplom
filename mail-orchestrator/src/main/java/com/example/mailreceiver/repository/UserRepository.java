package com.example.mailreceiver.repository;

import java.util.List;
import java.util.Optional;
import com.example.mailreceiver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByCompanyIdAndEnabledFalse(Long companyId);
    boolean existsByEmail(String email);
    List<User> findByCompanyId(Long companyId);

}