package com.example.service;

import com.example.dto.StudentDto;
import com.example.exception.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface StudentManagementService {

    public Boolean studentAccess(StudentDto studentDto)throws ServiceException;

    List<StudentDto> listStudent() throws ServiceException;

    StudentDto findStudentById(String id) throws ServiceException;

    void addStudent(StudentDto studentDto)throws  ServiceException;

    void modifyStudent(String id,StudentDto studentDto) throws ServiceException;

    void deleteStudent(String id) throws  ServiceException;
}
