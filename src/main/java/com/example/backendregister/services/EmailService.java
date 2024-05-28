package com.example.backendregister.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void  sendMail(String to, String subject, String text){
        SimpleMailMessage mailMessage = new SimpleMailMessage();


        mailMessage.setFrom("zeyadmo1911@gmail.com");
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        mailSender.send(mailMessage);

        System.out.println("email sent successfully");
    }

}
