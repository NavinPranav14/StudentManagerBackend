package com.example.service.impl;

import com.example.dao.StudentManagementDao;
import com.example.dao.impl.StudentManagementDaoImpl;
import com.example.dto.StaffDto;
import com.example.dto.StudentDto;
import com.example.entity.Staff;
import com.example.entity.Student;
import com.example.exception.MongoDbException;
import com.example.exception.NotFoundException;
import com.example.exception.ServiceException;
import com.example.service.StudentManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentManagementServiceImpl implements StudentManagementService {

    @Autowired
    private StudentManagementDao studentManagementDao;
    @Autowired
    private StudentManagementDaoImpl studentManagementDaoImpl;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public Boolean studentAccess(StudentDto studentDto) {
        Student student = studentManagementDao.studentAccess(studentDto.getUsername());
        return passwordEncoder.matches(studentDto.getPassword(), student.getPassword());
    }


    @Override
    public List<StudentDto> listStudent() throws ServiceException {
        List<StudentDto> studentDtoList = new ArrayList<>();
        try {
            List<Student> studentList = studentManagementDao.listStudent();
            studentList.parallelStream().forEach(student -> {
                StudentDto studentDto = new StudentDto();
                if(student.getStatus().equals("ACTIVE")) {
                    studentDto.setImageURL(student.getImageURL());
                    studentDto.setUsername(student.getUsername());
                    studentDto.setPassword(student.getPassword());
                    studentDto.setId(student.getId());
                    studentDto.setRoll(student.getRoll());
                    studentDto.setName(student.getName());
                    studentDto.setDepartment(student.getDepartment());
                    studentDto.setAdmissionDate(student.getAdmissionDate());
                    studentDtoList.add(studentDto);
                }
            });
        } catch (MongoDbException mongoDbException) {
            throw new ServiceException("Error while processing data", mongoDbException);
        }
        return studentDtoList;
    }

    @Override
    public StudentDto findStudentById(String id) throws ServiceException {
        StudentDto studentDto = new StudentDto();
        try {
            Student student = studentManagementDao.findStudentById(id);
            if(student.getStatus().equals("ACTIVE")) {
                studentDto.setImageURL(student.getImageURL());
                studentDto.setId(student.getId());
                studentDto.setUsername(student.getUsername());
                studentDto.setPassword(student.getPassword());
                studentDto.setRoll(student.getRoll());
                studentDto.setName(student.getName());
                studentDto.setDepartment(student.getDepartment());
                studentDto.setAdmissionDate(student.getAdmissionDate());
            }
        } catch (NotFoundException notFoundException) {
            throw new ServiceException("Student not found", notFoundException);
        }
        return studentDto;
    }

    @Override
    public void addStudent(StudentDto studentDto) throws ServiceException {
        Student student = new Student();
            if((studentDto.getUsername().length() > 3 ) && (studentDto.getPassword().length() > 3) && (studentManagementDaoImpl.findStudentByUserName(studentDto.getUsername()) == null)){
                String encodedPassword = passwordEncoder.encode(studentDto.getPassword());
                student.setUsername(studentDto.getUsername());
                LocalDateTime localDateTime = LocalDateTime.now();
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                student.setPassword(encodedPassword);
                student.setImageURL(studentDto.getImageURL());
                student.setName(studentDto.getName());
                student.setRoll(studentDto.getRoll());
                student.setAdmissionDate(dateTimeFormatter.format(localDateTime));
                student.setDepartment(studentDto.getDepartment());
                student.setStatus("ACTIVE");
//                student.setStatus(UserStatus.ACTIVE);
                studentManagementDao.addStudent(student);
            }else{
                throw new ServiceException("Student not added");
            }

    }

    @Override
    public void modifyStudent(String id, StudentDto studentDto) {
        Student student = new Student();
//        student.setUsername(studentDto.getUsername());
//        student.setPassword(studentDto.getPassword());
        student.setImageURL(studentDto.getImageURL());
//        student.setAdmissionDate(studentDto.getAdmissionDate());
        student.setName(studentDto.getName());
        student.setRoll(studentDto.getRoll());
        student.setDepartment(studentDto.getDepartment());
        studentManagementDao.modifyStudent(student, id);
        return;
    }

    @Override
    public void deleteStudent(String id)  throws  ServiceException{
        try {
            studentManagementDao.deleteStudent(id);
        }catch (Exception e){
            throw new ServiceException("Student with this id is not found",e);
        }
        return ;
    }

}
