package com.example.dao;

import com.example.entity.Student;
import com.example.exception.MongoDbException;
import com.example.exception.NotFoundException;

import java.util.List;

public interface StudentManagementDao {

    Student studentAccess(String username);

    List<Student> listStudent() throws MongoDbException;

    Student findStudentById(String id) throws NotFoundException;

    public Student findStudentByUserName(String username);

    void addStudent(Student student);

    void modifyStudent(Student student, String id);

    void deleteStudent(String id) throws NotFoundException;
}
