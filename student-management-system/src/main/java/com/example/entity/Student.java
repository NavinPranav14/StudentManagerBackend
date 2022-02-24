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
@Document(collection = "student")
public class Student {

    @Field("username")
    @Email
    @NotBlank
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

    @Field("roll")
    @NotBlank
    private String roll;

    @Field("name")
    @NotBlank
    private String name;

    @Field("department")
    @NotBlank
    private String department;

    @Field("admission")
    private String admissionDate;

    @Field("status")
    private String status;

}
