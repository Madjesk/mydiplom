package com.example.mailreceiver.service;

import com.example.mailreceiver.dto.Email;
import com.example.mailreceiver.dto.MarkInvalidRequest;
import com.example.mailreceiver.entity.MailCredential;
import com.example.mailreceiver.repository.MailCredentialRepository;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMultipart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.internet.InternetAddress;

import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class GemailReceiverService {
    private final MailCredentialRepository credentialRepo;
    private final RestTemplate restTemplate = new RestTemplate();

    private final String statsUrl = "http://localhost:8082/api/stats/markInvalid";

    private final Map<Long, Integer> lastCount = new HashMap<>();

    public GemailReceiverService(MailCredentialRepository credentialRepo) {
        this.credentialRepo = credentialRepo;
    }


    @Scheduled(fixedRateString = "PT1M")
    public void pollAllCompanies() {
        List<MailCredential> creds = credentialRepo.findAll();
        for (MailCredential mc : creds) {
            try {
                pollInbox(mc);
            } catch (Exception ex) {
                log.error("Не удалось прочитать почту {}: {}", mc.getEmail(), ex.getMessage());
            }
        }
    }


    private void pollInbox(MailCredential cred) throws MessagingException, IOException {
        Properties props = new Properties();
        props.put("mail.store.protocol", "imap");
        props.put("mail.imap.host", cred.getImapHost());
        props.put("mail.imap.port", String.valueOf(cred.getImapPort()));
        props.put("mail.imap.ssl.enable", "true");

        Session session = Session.getInstance(props);
        try (Store store = session.getStore("imap")) {

            store.connect(cred.getImapHost(), cred.getEmail(), cred.getAppPassword());
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            int prev = lastCount.getOrDefault(cred.getCompany().getId(), 0);
            int curr = inbox.getMessageCount();

            if (curr > prev) {
                Message[] msgs = inbox.getMessages(prev + 1, curr);
                log.info("[{}] найдено {} новых писем", cred.getEmail(), msgs.length);

                for (Message m : msgs) {
                    processMessage(m, cred.getCompany().getId());
                }
                lastCount.put(cred.getCompany().getId(), curr);
            }
        }
    }

    /* ============================================================ */

    private void processMessage(Message message, Long companyId) throws IOException, MessagingException {

        Email email = new Email();
        email.setFrom(InternetAddress.toString(message.getFrom()));
        email.setSubject(message.getSubject());
        email.setReceiveDate(message.getReceivedDate());
        email.setContent(getTextFromMessage(message));

        if (isUndeliveredMail(email)) {
            String bounced = parseBouncedEmail(email.getContent());
            if (bounced != null) markInvalid(bounced, companyId);
        }
    }

    private void markInvalid(String bouncedEmail, Long companyId) {
        try {
            MarkInvalidRequest req = new MarkInvalidRequest();
            req.setMailingId(lookupLastMailingId(companyId));
            req.setEmail(bouncedEmail);
            restTemplate.postForEntity(statsUrl, req, Void.class);
            log.info("Пометили {} недоставленным (company {})", bouncedEmail, companyId);
        } catch (Exception e) {
            log.error("Не смогли вызвать stats service: {}", e.getMessage());
        }
    }



    public String getTextFromMessage(Message message) throws MessagingException, IOException {
        if(message.isMimeType("text/plain")) {
            return (String) message.getContent();
        } else if(message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            return getTextFromMimeMultipart(mimeMultipart);
        }
        return "";
    }

    public String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < mimeMultipart.getCount(); i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                stringBuilder.append(bodyPart.getContent());
            }
        }
        return stringBuilder.toString();
    }

    private boolean isUndeliveredMail(Email email) {
        String from = email.getFrom() != null ? email.getFrom().toLowerCase() : "";
        String subj = email.getSubject() != null ? email.getSubject().toLowerCase() : "";
        if (from.contains("mailer-daemon") || subj.contains("Delivery")) {
            return true;
        }
        return false;
    }

    private String parseBouncedEmail(String content) {
        if(content == null) return null;
        String lower = content.toLowerCase();
        int idx = lower.indexOf("original recipient");
        if (idx != -1) {
            int colon = content.indexOf(":", idx);
            if(colon != -1 && colon + 1 < content.length()) {
                String rest = content.substring(colon + 1).trim();
                String[] tokens = rest.split("\\s+");
                if(tokens.length > 0) {
                    return tokens[0].replace("<","").replace(">","");
                }
            }
        }
        return null;
    }
}
