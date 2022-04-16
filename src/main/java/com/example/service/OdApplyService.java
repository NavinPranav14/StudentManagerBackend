package com.example.service;

import com.example.dto.OdApplyDto;
import com.example.entity.OdApply;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OdApplyService {

    void odApply(OdApplyDto odApplyDto);

    void updateStatus(String id, OdApplyDto odApplyDto);

    List<OdApply>  getall();

    OdApplyDto getById(String id);
}
