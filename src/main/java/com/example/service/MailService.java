package com.example.service;

import com.example.dto.OdApplyDto;
import org.springframework.stereotype.Service;

@Service
public interface MailService {
    void odEmailService(OdApplyDto odApplyDto);

    void odStatusMail();
}
