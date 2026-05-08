package com.siemens.train_ticketing.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public void sendEmail(String to, String subject, String body) {
        System.out.println("MOCK EMAIL SENT!");
        System.out.println("TO: " + to);
        System.out.println("SUBJECT: " + subject);
        System.out.println("BODY: " + body);
    }
}
