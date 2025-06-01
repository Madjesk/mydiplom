package com.example.bussinessmailsender.dto;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.List;

public class ScheduledMail {
    private List<String> recipients;
    private String subject;
    private String message;
    private LocalDateTime scheduledTime; // когда отправлять
    private boolean sent; // уже отправлено или нет

    public ScheduledMail(List<String> recipients, String subject, String message, LocalDateTime scheduledTime) {
        this.recipients = recipients;
        this.subject = subject;
        this.message = message;
        this.scheduledTime = scheduledTime;
        this.sent = false;
    }


    public boolean isTimeToSend() {
        return LocalDateTime.now().isAfter(scheduledTime) || LocalDateTime.now().isEqual(scheduledTime);
    }

    public void send(JavaMailSender mailSender) {
        if (recipients != null) {
            for (String to : recipients) {
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(to);
                mailMessage.setSubject(subject);
                mailMessage.setText(message);
                mailSender.send(mailMessage);
            }
        }
        this.sent = true;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }
}
