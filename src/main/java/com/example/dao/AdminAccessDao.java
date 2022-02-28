package com.example.dao;

import com.example.entity.Admin;
import com.example.exception.NotFoundException;

public interface AdminAccessDao {

    public Admin adminAccess(String username) throws NotFoundException;
}
