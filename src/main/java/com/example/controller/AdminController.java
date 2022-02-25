package com.example.controller;


import com.example.controller.response.APIResponse;
import com.example.dto.AdminDto;
import com.example.exception.NotFoundException;
import com.example.exception.ServiceException;
import com.example.service.AdminService;
import com.example.util.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.example.enums.APIResponseStatus.FAILURE;
import static com.example.enums.APIResponseStatus.SUCCESS;

@RestController
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtility jwtUtility;

    @PostMapping("/admin/login")
    public ResponseEntity<APIResponse> getAdminAccess(@RequestBody AdminDto adminDto) throws ServiceException {
        APIResponse response = new APIResponse();

            if(adminService.adminAccess(adminDto)){
                response.setStatus(SUCCESS.getDisplayName());
                response.setMessage("Login success");
                response.setData(jwtUtility.generateToken(adminDto));
                return new ResponseEntity<APIResponse>(response, HttpStatus.OK);
            }
            else{
                response.setStatus(FAILURE.getDisplayName());
                response.setMessage("Login failed");
                return new ResponseEntity<APIResponse>(response, HttpStatus.OK);
            }


    }
}