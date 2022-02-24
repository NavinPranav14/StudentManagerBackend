package com.example.dao;

import com.example.entity.Staff;
import com.example.exception.MongoDbException;
import com.example.exception.NotFoundException;

import java.util.List;
public interface StaffManagementDao {

    String _ID = "_id";

    Staff staffAccess(String username);

    public Staff findStaffByUserName(String username);

    List<Staff> listStaff() throws MongoDbException;

    Staff findStaffById(String id) throws NotFoundException;

    void addStaff(Staff staff);

    void modifyStaff(Staff staff, String id);

    void deleteStaff(String id) throws NotFoundException;
}

