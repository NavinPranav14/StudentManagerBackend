package com.example.service.impl;

import com.example.dao.StaffManagementDao;
import com.example.dao.impl.StaffManagementDaoImpl;
import com.example.dto.StaffDto;
import com.example.entity.Staff;
import com.example.exception.MongoDbException;
import com.example.exception.NotFoundException;
import com.example.exception.ServiceException;
import com.example.service.StaffManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.enums.UserStatus.ACTIVE;

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
            throw new ServiceException("Error while processing data");

        }
    }

    @Override
    public List<StaffDto> listStaff() throws ServiceException {
        List<StaffDto> staffDtoList = new ArrayList<>();
        try {
            List<Staff> staffList = staffManagementDao.listStaff();
            staffList.parallelStream().forEach(staff -> {
                    StaffDto staffDto = new StaffDto();
                    if(staff.getStatus().equals(ACTIVE)){
                    staffDto.setImageURL(staff.getImageURL());
                    staffDto.setUsername(staff.getUsername());
                    staffDto.setPassword(staff.getPassword());
                    staffDto.setId(staff.getId());
                    staffDto.setName(staff.getName());
                    staffDto.setDepartment(staff.getDepartment());
                    staffDto.setStaffID(staff.getStaffID());
                    staffDtoList.add(staffDto);
                }
            });
        } catch (MongoDbException mongoDbException) {
            throw new ServiceException("Error while processing data", mongoDbException);
        }
        return staffDtoList;
    }

    @Override
    public StaffDto findStaffById(String id) throws ServiceException {
        StaffDto staffDto = new StaffDto();
        try {
            Staff staff = staffManagementDao.findStaffById(id);
            if(staff.getStatus().equals(ACTIVE)) {
                staffDto.setImageURL(staff.getImageURL());
                staffDto.setId(staff.getId());
                staffDto.setName(staff.getName());
                staffDto.setDepartment(staff.getDepartment());
                staffDto.setStaffID(staff.getStaffID());
                staffDto.setUsername(staff.getUsername());
                staffDto.setPassword(staff.getPassword());
            }
        } catch (NotFoundException notFoundException) {
            throw new ServiceException("Staff not found", notFoundException);
        }
        return staffDto;
    }

    @Override
    public void addStaff(StaffDto staffDto) throws ServiceException {
        Staff staff = new Staff();

            if((staffDto.getUsername().length() > 3 ) && (staffDto.getPassword().length() > 3) && (staffManagementDaoImpl.findStaffByUserName(staffDto.getUsername()) == null)){
                GenerateId generate = new GenerateId();
                String encodedPassword = passwordEncoder.encode(staffDto.getPassword());
                staff.setName(staffDto.getName());
                staff.setDepartment(staffDto.getDepartment());
                staff.setStaffID(generate.generateId("STF"));
                staff.setId(staffDto.getId());
                staff.setUsername(staffDto.getUsername());
                staff.setImageURL(staffDto.getImageURL());
                staff.setPassword(encodedPassword);
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
        staff.setId(staffDto.getId());
        staff.setImageURL(staffDto.getImageURL());
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
