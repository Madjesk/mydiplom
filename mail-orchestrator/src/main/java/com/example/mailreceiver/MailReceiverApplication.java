package com.example.mailreceiver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MailReceiverApplication {

    public static void main(String[] args) {
        SpringApplication.run(MailReceiverApplication.class, args);
    }

}
