package com.example.service.impl;

import com.example.dao.AdminAccessDao;
import com.example.dto.AdminDto;
import com.example.entity.Admin;
import com.example.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements  AdminService {
    @Autowired
    private AdminAccessDao adminAccessDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public Boolean adminAccess(AdminDto adminDto) {
        Admin admin = adminAccessDao.adminAccess(adminDto.getUsername());
        return passwordEncoder.matches(adminDto.getPassword(), admin.getPassword());
    }
}
