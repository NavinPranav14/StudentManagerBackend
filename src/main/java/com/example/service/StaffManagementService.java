package com.example.service;

import com.example.dto.StaffDto;
import com.example.entity.Staff;
import com.example.exception.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StaffManagementService {

    public Boolean staffAccess(StaffDto staffDto) throws ServiceException;

    List<Staff> listStaff() throws ServiceException;

    List<Staff> findStaffByName(String name) throws ServiceException;

    StaffDto findStaffById(String id) throws ServiceException;

    void addStaff(StaffDto staffDto)throws  ServiceException;

    void modifyStaff(String id, StaffDto staffDto) throws ServiceException;

    void deleteStaff(String id) throws  ServiceException;

    StaffDto findStaffByUsername(String username) throws ServiceException;

    StaffDto findStaffForPassword(String username, String generated) throws ServiceException;

    void changeStaffPassword(String jwt, String password) throws ServiceException;

}
