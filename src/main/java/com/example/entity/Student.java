package com.example.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Student {

    @Field("username")
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",message = "Must be a valid email")
    @NotBlank
    private String username;

    @Field("password")
    @NotBlank
    @Size(min = 3)
    private String password;

    @Field("imageURL")
    private String imageURL;

    @Field("_id")
    private String id;

    @Field("roll")
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

    @Field("gender")
    private String gender;

    @Field("dob")
    private String dob;

}
