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
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "staff")
public class Staff {
    @Field("username")
    @NotBlank
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",message = "Must be a valid email")
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

    @Field("phone")
    @Size(min = 10)
    private String phone;

    @Field("status")
    private String status;

    @Field("gender")
    private String gender;

        @Field("dob")
    private String dob;

}