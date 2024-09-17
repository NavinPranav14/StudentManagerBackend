package com.example.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "onDuty")
public class OdApply {

    private String id;

    @Field("name")
    private String name;

    @Field("date")
    private String date;

    @Field("checkIn")
    private String checkIn;

    @Field("checkOut")
    private String checkOut;

    @Field("mentor")
    private String mentor;

    @Field("reason")
    private String reason;

    @Field("status")
    private String status;
}
