package com.example.dao;

import com.example.entity.Staff;
import com.example.exception.MongoDbException;
import com.example.exception.NotFoundException;

import java.util.List;
public interface StaffManagementDao {


    Staff staffAccess(String username);

    Staff findStaffByUserName(String username) throws NotFoundException;

    List<Staff> findStaffByName(String name) throws MongoDbException;

    List<Staff> listStaff() throws MongoDbException;

    Staff findStaffById(String id) throws NotFoundException;

    void addStaff(Staff staff);

    void modifyStaff(Staff staff, String id);

    void deleteStaff(String id) throws NotFoundException;

    void resetStaffPassword(String id, String password) throws NotFoundException;
}

