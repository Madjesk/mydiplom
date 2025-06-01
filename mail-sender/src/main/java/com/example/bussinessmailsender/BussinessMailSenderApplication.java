package com.example.bussinessmailsender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableScheduling
@Slf4j
public class BussinessMailSenderApplication {

    public static void main(String[] args) {
        log.info("sdfa");
        SpringApplication.run(BussinessMailSenderApplication.class, args);
    }

}
