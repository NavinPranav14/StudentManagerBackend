package com.example.service.impl;

import com.example.dto.OdApplyDto;
import com.example.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void odEmailService(OdApplyDto odApplyDto){

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("navinpranav1234@gmail.com");
        message.setTo("navinpranav1234@gmail.com");
        message.setText("The student "+ odApplyDto.getName() + " has been applied for On duty on the following day " + odApplyDto.getDate() + " from " + odApplyDto.getCheckOut() + " to " + odApplyDto.getCheckIn() + " reason: " + odApplyDto.getReason());
        message.setSubject("OD apply");
        javaMailSender.send(message);
    }

    @Override
    public void odStatusMail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("navinpranav1234@gmail.com");
        message.setTo("navinpranav1234@gmail.com");
        message.setText("sample text");
        message.setSubject("OD Status");
        javaMailSender.send((message));
    }
}
