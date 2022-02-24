package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "staff")
public class Staff {
    @Field("username")
    @NotBlank
    @Email
    private String username;

    @Field("password")
    @NotBlank
    @Size(min = 3)
    private String password;

    @Field("imageURL")
    private String imageURL;

    @Field("_id")
    @NotBlank
    private String id;

    @Field("name")
    @NotBlank
    private String name;

    @Field("department")
    @NotBlank
    private String department;

    @Field("staffID")
    private String staffID;

    @Field("status")
    private String status;

}