package com.example.service;

import com.example.dto.StaffDto;
import com.example.dto.StudentDto;
import com.example.entity.Staff;
import com.example.exception.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StaffManagementService {

    public Boolean staffAccess(StaffDto staffDto);

    List<StaffDto> listStaff() throws ServiceException;

    StaffDto findStaffById(String id) throws ServiceException;

    void addStaff(StaffDto staffDto)throws  ServiceException;

    void modifyStaff(String id, StaffDto staffDto);

    void deleteStaff(String id) throws  ServiceException;



}