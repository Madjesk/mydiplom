package com.example.mailstat.repository;

import com.example.mailstat.entity.MailingRecipient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailingRecipientRepository extends JpaRepository<MailingRecipient, Long> {
    MailingRecipient findByEmailAndMailingHistory_Id(String email, Long mailingHistoryId);
}
