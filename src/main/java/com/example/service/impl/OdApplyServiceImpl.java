package com.example.service.impl;

import com.example.dao.OdApplyRespository;
import com.example.dto.OdApplyDto;
import com.example.entity.OdApply;
import com.example.service.OdApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OdApplyServiceImpl implements OdApplyService {

    @Autowired
    private OdApplyRespository odApplyRespository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void odApply(OdApplyDto odApplyDto) {
        OdApply odApply = new OdApply();
        SimpleMailMessage message = new SimpleMailMessage();
        odApply.setName(odApplyDto.getName());
        odApply.setDate(odApplyDto.getDate());
        odApply.setCheckIn(odApplyDto.getCheckIn());
        odApply.setCheckOut(odApplyDto.getCheckOut());
        odApply.setMentor(odApplyDto.getMentor());
        odApply.setReason(odApplyDto.getReason());
        odApply.setStatus("null");
        odApplyRespository.insert(odApply);
        message.setFrom("navinpranav1234@gmail.com");
        message.setTo("navinpranav1234@gmail.com");
        message.setText("The student "+ odApplyDto.getName() + " has been applied for On duty on the following day " + odApplyDto.getDate() + " from " + odApplyDto.getCheckOut() + " to " + odApplyDto.getCheckIn() + " reason: " + odApplyDto.getReason());
        message.setSubject("OD apply");
        javaMailSender.send(message);
    }

    @Override
    public void updateStatus(String id, OdApplyDto odApplyDto) {
        Optional<OdApply> odApplyOptional = odApplyRespository.findById(id);
        OdApply odApply = odApplyOptional.get();
        odApply.setStatus(odApplyDto.getStatus());
        odApplyRespository.save(odApply);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("navinpranav1234@gmail.com");
        message.setTo("navinpranav1234@gmail.com");
        message.setText("The OD you have applied is been " + odApply.getStatus());
        message.setSubject("OD Status");
        javaMailSender.send((message));
    }

    @Override
    public List<OdApply> getall() {
        return odApplyRespository.findAll();
    }

    @Override
    public OdApplyDto getById(String id) {
        Optional<OdApply>  odApplyOptional = odApplyRespository.findById(id);
        OdApply odApply = odApplyOptional.get();
        OdApplyDto odApplyDto = new OdApplyDto();
        odApplyDto.setName(odApply.getName());
        odApplyDto.setDate(odApply.getDate());
        odApplyDto.setCheckIn(odApply.getCheckIn());
        odApplyDto.setCheckOut(odApply.getCheckOut());
        odApplyDto.setMentor(odApply.getMentor());
        odApplyDto.setReason(odApply.getReason());
        odApplyDto.setStatus("null");
        return odApplyDto;
    }
}

