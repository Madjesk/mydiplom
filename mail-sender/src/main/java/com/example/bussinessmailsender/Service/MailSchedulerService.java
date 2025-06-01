package com.example.bussinessmailsender.Service;

import com.example.bussinessmailsender.dto.GroupEmailRequestDto;
import com.example.bussinessmailsender.dto.ScheduledMail;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class MailSchedulerService {

    private List<ScheduledMail> scheduledMails = new CopyOnWriteArrayList<>();

    private final JavaMailSender mailSender;

    public MailSchedulerService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void scheduleMail(GroupEmailRequestDto dto) {
        LocalDateTime dateTime = parseDateTime(dto.getSendDate(), dto.getSendTime());

        ScheduledMail task = new ScheduledMail(
                dto.getEmails(),
                dto.getSubject(),
                dto.getMessage(),
                dateTime
        );
        scheduledMails.add(task);
    }

    @Scheduled(fixedRate = 30000)
    public void checkAndSend() {
        for (ScheduledMail sm : scheduledMails) {
            if (!sm.isSent() && sm.isTimeToSend()) {
                sm.send(mailSender);
            }
        }
    }

    private LocalDateTime parseDateTime(String sendDate, String sendTime) {
        try {
            if (sendDate == null || sendDate.isEmpty()) {
                return LocalDateTime.now();
            }
            LocalDate date = LocalDate.parse(sendDate);
            LocalTime time = LocalTime.MIDNIGHT;
            if (sendTime != null && !sendTime.isEmpty()) {
                time = LocalTime.parse(sendTime);
            }
            return LocalDateTime.of(date, time);
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }
}
