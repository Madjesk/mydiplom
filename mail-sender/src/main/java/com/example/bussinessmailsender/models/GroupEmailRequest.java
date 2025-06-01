package com.example.bussinessmailsender.models;
import lombok.Data;

import java.util.List;

@Data
public class GroupEmailRequest {
    private String groupName;
    private String subject;
    private String message;

    private List<String> emails;

    private String sendDate;
    private String sendTime;
}
