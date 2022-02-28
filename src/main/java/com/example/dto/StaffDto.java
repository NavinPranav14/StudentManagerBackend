package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

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
