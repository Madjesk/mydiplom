package com.example.mailstat;

import com.example.mailstat.dto.MailingHistoryRequest;
import com.example.mailstat.dto.MailingHistoryResponse;
import com.example.mailstat.dto.MarkInvalidRequest;
import com.example.mailstat.entity.MailingHistory;
import com.example.mailstat.entity.MailingRecipient;
import com.example.mailstat.repository.MailingHistoryRepository;
import com.example.mailstat.repository.MailingRecipientRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatsService {

    private final MailingHistoryRepository mailingHistoryRepository;
    private final MailingRecipientRepository mailingRecipientRepository;

    public StatsService(MailingHistoryRepository mailingHistoryRepository,
                        MailingRecipientRepository mailingRecipientRepository) {
        this.mailingHistoryRepository = mailingHistoryRepository;
        this.mailingRecipientRepository = mailingRecipientRepository;
    }

    public MailingHistoryResponse saveMailing(MailingHistoryRequest request) {
        MailingHistory history = new MailingHistory();
        history.setCompanyId(request.getCompanyId());
        history.setSubject(request.getSubject());
        history.setMessage(request.getMessage());
        history.setGroupName(request.getGroupName());
        history.setSendDate(request.getSendDate() != null ? request.getSendDate() : LocalDateTime.now());

        int total = request.getEmails() != null ? request.getEmails().size() : 0;
        history.setTotalRecipients(total);

        int failed = request.getInvalidEmails() != null ? request.getInvalidEmails().size() : 0;
        history.setFailedRecipientsCount(failed);

        if (request.getEmails() != null) {
            for (String email : request.getEmails()) {
                MailingRecipient r = new MailingRecipient();
                r.setEmail(email);
                boolean isInvalid = request.getInvalidEmails() != null && request.getInvalidEmails().contains(email);
                r.setDelivered(!isInvalid);
                r.setMailingHistory(history);
                history.getRecipients().add(r);
            }
        }

        MailingHistory saved = mailingHistoryRepository.save(history);
        return toResponse(saved);
    }

    public List<MailingHistoryResponse> getAllMailings(Long companyId) {
        return mailingHistoryRepository.findAllByCompanyId(companyId)
                .stream().map(this::toResponse).toList();
    }

    public MailingHistoryResponse getMailing(Long id, Long companyId) {
        return mailingHistoryRepository.findByIdAndCompanyId(id, companyId)
                .map(this::toResponse).orElse(null);
    }

    public void markInvalid(MarkInvalidRequest request) {
        MailingRecipient recipient = mailingRecipientRepository
                .findByEmailAndMailingHistory_Id(request.getEmail(), request.getMailingId());
        if (recipient != null && recipient.isDelivered()) {
            recipient.setDelivered(false);
            mailingRecipientRepository.save(recipient);

            MailingHistory history = recipient.getMailingHistory();
            history.incrementFailedCount();
            mailingHistoryRepository.save(history);
        }
    }

    private MailingHistoryResponse toResponse(MailingHistory entity) {
        MailingHistoryResponse resp = new MailingHistoryResponse();
        resp.setMailingId(entity.getId());
        resp.setSubject(entity.getSubject());
        resp.setMessage(entity.getMessage());
        resp.setGroupName(entity.getGroupName());
        resp.setSendDate(entity.getSendDate());
        resp.setTotalRecipients(entity.getTotalRecipients());
        resp.setFailedRecipientsCount(entity.getFailedRecipientsCount());

        List<MailingHistoryResponse.RecipientDto> recipients = new ArrayList<>();
        for (MailingRecipient r : entity.getRecipients()) {
            MailingHistoryResponse.RecipientDto rd = new MailingHistoryResponse.RecipientDto();
            rd.setEmail(r.getEmail());
            rd.setDelivered(r.isDelivered());
            recipients.add(rd);
        }
        resp.setRecipients(recipients);
        return resp;
    }
}
