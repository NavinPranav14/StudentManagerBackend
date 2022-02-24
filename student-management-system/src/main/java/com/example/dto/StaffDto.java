package com.example.dto;

import lombok.Data;

import javax.validation.constraints.Email;

@Data

public class StaffDto {

    private String id;
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",message = "Must be a valid email")

    private String username;


    private String password;

    private String staffID;

    private String name;

    private String department;

    private String imageURL;

    private String status;

}
