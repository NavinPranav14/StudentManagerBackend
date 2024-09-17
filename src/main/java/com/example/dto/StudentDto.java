package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    private String gender;

    private String dob;

}
