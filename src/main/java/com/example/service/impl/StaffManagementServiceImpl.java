package com.example.service.impl;

import com.example.dao.StaffManagementDao;
import com.example.dao.impl.StaffManagementDaoImpl;
import com.example.dto.StaffDto;
import com.example.entity.Staff;
import com.example.enums.UserStatus;
import com.example.exception.MongoDbException;
import com.example.exception.NotFoundException;
import com.example.exception.ServiceException;
import com.example.service.StaffManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffManagementServiceImpl implements StaffManagementService {
    @Autowired
    private StaffManagementDao staffManagementDao;

    @Autowired
    private StaffManagementDaoImpl staffManagementDaoImpl;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Boolean staffAccess(StaffDto staffDto) throws ServiceException{
        try{
            Staff staff = staffManagementDao.staffAccess(staffDto.getUsername());
            return passwordEncoder.matches(staffDto.getPassword(), staff.getPassword());
        }catch (Exception e){
            throw new ServiceException("Username or password is incorrect");

        }
    }

    @Override
    public List<Staff> listStaff() throws ServiceException {
        try {
            List<Staff> staffList = staffManagementDao.listStaff();
            return staffList;
        } catch (MongoDbException mongoDbException) {
            throw new ServiceException("data not found", mongoDbException);
        }
    }

    @Override
    public List<Staff> findStaffByName(String name) throws ServiceException {
        try {

            List<Staff> staffList = staffManagementDao.findStaffByName(name);

                return staffList;

        } catch (MongoDbException mongoDbException) {
            throw new ServiceException("user not found", mongoDbException);
        }
    }



    @Override
    public StaffDto findStaffById(String id) throws ServiceException {
        StaffDto staffDto = new StaffDto();
        try {
            Staff staff = staffManagementDao.findStaffById(id);
            if(staff.getStatus().equals(UserStatus.ACTIVE.name())) {
                staffDto.setImageURL(staff.getImageURL());
                staffDto.setId(staff.getId());
                staffDto.setName(staff.getName());
                staffDto.setDepartment(staff.getDepartment());
                staffDto.setStaffID(staff.getStaffID());
                staffDto.setUsername(staff.getUsername());
                staffDto.setPhone(staff.getPhone());
                staffDto.setGender(staff.getGender());
                staffDto.setDob(staff.getDob());
                staffDto.setPassword(staff.getPassword());
            }
        } catch (NotFoundException notFoundException) {
            throw new ServiceException("Staff not found", notFoundException);
        }
        return staffDto;
    }

    @Override
    public StaffDto findStaffByUsername(String username) throws ServiceException {
        StaffDto staffDto = new StaffDto();
        try {
            Staff staff = staffManagementDao.findStaffByUserName(username);
            if(staff.getStatus().equals(UserStatus.ACTIVE.name())) {
                staffDto.setImageURL(staff.getImageURL());
                staffDto.setId(staff.getId());
                staffDto.setName(staff.getName());
                staffDto.setDepartment(staff.getDepartment());
                staffDto.setStaffID(staff.getStaffID());
                staffDto.setUsername(staff.getUsername());
                staffDto.setPhone(staff.getPhone());
                staffDto.setGender(staff.getGender());
                staffDto.setDob(staff.getDob());
                staffDto.setPassword(staff.getPassword());
            }
        } catch (Exception e) {
            throw new ServiceException("Staff not found", e);
        }
        return staffDto;
    }


    @Override
    public void addStaff(StaffDto staffDto) throws ServiceException {
        Staff staff = new Staff();

            if((staffDto.getUsername().length() > 3 ) && (staffDto.getPassword().length() > 3) && (staffManagementDaoImpl.findStaffByUserName(staffDto.getUsername()) == null)){
                com.example.service.impl.GenerateId generate = new GenerateId();
                String encodedPassword = passwordEncoder.encode(staffDto.getPassword());
                staff.setName(staffDto.getName());
                staff.setDepartment(staffDto.getDepartment());
                staff.setStaffID(generate.generateId("STF"));
                staff.setId(staffDto.getId());
                staff.setUsername(staffDto.getUsername());
                staff.setImageURL(staffDto.getImageURL());
                staff.setPassword(encodedPassword);
                staff.setPhone(staffDto.getPhone());
                staff.setGender(staffDto.getGender());
                staff.setDob(staffDto.getDob());
                staff.setStatus("ACTIVE");
                try{
                    staffManagementDao.addStaff(staff);
                }catch (Exception e){
                    throw new ServiceException("Staff not added");
                }
            }
            else{
                throw new ServiceException("Invalid data");
            }
    }

    @Override
    public void modifyStaff(String id, StaffDto staffDto) throws ServiceException{
        Staff staff = new Staff();
        staff.setName(staffDto.getName());
        staff.setDepartment(staffDto.getDepartment());
        staff.setUsername(staffDto.getUsername());
        staff.setId(staffDto.getId());
        staff.setImageURL(staffDto.getImageURL());
        staff.setPhone(staffDto.getPhone());
        staff.setDob(staffDto.getDob());
        staff.setGender(staffDto.getGender());

        try{
            staffManagementDao.modifyStaff(staff, id);
        }catch (Exception e){
            throw new ServiceException("Staff with this id is not found",e);
        }
        return;
    }

    public void deleteStaff(String id)  throws  ServiceException{
        try {
            staffManagementDao.deleteStaff(id);
        }catch (Exception e){
            throw new ServiceException("Staff with this id is not found",e);
        }
        return ;
    }
}
