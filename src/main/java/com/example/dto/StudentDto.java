package com.example.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class StudentDto {

    private String id;
    @Email
    private String username;

    private String password;

    private String roll;

    private String name;

    private String department;

    private String admissionDate;

    private String imageURL;

    private String status;

}
