package com.example.dto;

import lombok.Data;

import javax.validation.constraints.Email;

@Data

public class StaffDto {

    private String id;

    private String username;


    private String password;

    private String staffID;

    private String name;

    private String department;

    private String imageURL;

    private String status;

}
