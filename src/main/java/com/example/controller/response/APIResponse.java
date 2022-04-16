package com.example.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class APIResponse {

    private String status;

    private String errorCode;

    private String message;

    private Object data;

    public APIResponse() {
    }
//
//    public APIResponse(String message, Object data) {
//        this.message = message;
//        this.data = data;
//    }
//
//    public APIResponse(String status, String message) {
//        this.status = status;
//        this.message = message;
//    }
}
