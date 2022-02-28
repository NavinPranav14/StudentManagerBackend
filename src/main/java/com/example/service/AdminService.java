package com.example.service;

import com.example.dto.AdminDto;
import com.example.exception.ServiceException;
import org.springframework.stereotype.Service;

@Service
public interface AdminService {

    public Boolean adminAccess(AdminDto adminDto) throws ServiceException;

}
