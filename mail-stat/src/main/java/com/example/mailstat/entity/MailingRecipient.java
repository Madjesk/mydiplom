package com.example.mailstat.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "mailing_recipient")
public class MailingRecipient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private boolean delivered;

    @ManyToOne
    @JoinColumn(name = "mailing_history_id")
    private MailingHistory mailingHistory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public MailingHistory getMailingHistory() {
        return mailingHistory;
    }

    public void setMailingHistory(MailingHistory mailingHistory) {
        this.mailingHistory = mailingHistory;
    }

}