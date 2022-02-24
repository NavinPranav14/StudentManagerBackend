package com.example.controller;

import com.example.controller.response.APIResponse;
import com.example.dto.StaffDto;
import com.example.dto.StudentDto;
import com.example.exception.ServiceException;
import com.example.service.StudentManagementService;
import com.example.util.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.enums.APIResponseStatus.FAILURE;
import static com.example.enums.APIResponseStatus.SUCCESS;

@RestController
public class StudentManagementController {

    @Autowired
    private StudentManagementService studentManagementService;
    @Autowired
    private JwtUtility jwtUtility;

    @PostMapping("/student/login")
    public ResponseEntity<APIResponse> getStudentAccess(@RequestBody StudentDto studentDto){
        APIResponse response = new APIResponse();

        if(studentManagementService.studentAccess(studentDto)){
            response.setStatus(SUCCESS.getDisplayName());
            response.setMessage("Login success");
            response.setData(jwtUtility.generateToken(studentDto));
            return new ResponseEntity<APIResponse>(response, HttpStatus.OK);
        }
        else{
            response.setStatus(FAILURE.getDisplayName());
            response.setMessage("Login failed");
            return new ResponseEntity<APIResponse>(response, HttpStatus.OK);
        }

    }

    @GetMapping("/student/all")
    public ResponseEntity<APIResponse> listStudent()
            throws ServiceException {
        APIResponse response = new APIResponse();
            response.setStatus(SUCCESS.getDisplayName());
            response.setData(studentManagementService.listStudent());
            return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/student/{id}")
    public ResponseEntity<APIResponse> findStudentById(@PathVariable String id)
            throws ServiceException {
        APIResponse response = new APIResponse();
            response.setStatus(SUCCESS.getDisplayName());
            response.setData(studentManagementService.findStudentById(id));
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/student/create")
    public ResponseEntity<APIResponse> addStudent(@Valid @RequestHeader(value = "authorization") String auth, @RequestBody StudentDto studentDto)
            throws ServiceException {
        APIResponse response = new APIResponse();
        if(jwtUtility.validateAdminToken(auth)!=null || jwtUtility.validateStaffToken(auth) != null) {
                response.setStatus(SUCCESS.getDisplayName());
                studentManagementService.addStudent(studentDto);
                return new ResponseEntity<APIResponse>(response, HttpStatus.OK);
        }
        else{
            response.setStatus(FAILURE.getDisplayName());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        }
    }

    @PutMapping("/student/edit/{id}")

    public ResponseEntity<APIResponse> modifyStudent(@RequestHeader(value = "authorization") String auth, @PathVariable String id,
                                                     @Valid @RequestBody StudentDto studentDto){
        APIResponse response = new APIResponse();
        if(jwtUtility.validateAdminToken(auth)!=null || jwtUtility.validateStaffToken(auth) != null) {
            response.setStatus(SUCCESS.getDisplayName());
            studentManagementService.modifyStudent(id, studentDto);
            return new ResponseEntity<APIResponse>(response, HttpStatus.OK);
        } else {
            System.out.println("invalid user");
            response.setStatus(FAILURE.getDisplayName());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/student/delete/{id}")
    public ResponseEntity<APIResponse> deleteStudent(@RequestHeader(value = "authorization") String auth, @PathVariable String id) throws  ServiceException {
        APIResponse response = new APIResponse();
        if(jwtUtility.validateAdminToken(auth)!=null || jwtUtility.validateStaffToken(auth) != null) {
            try {
                studentManagementService.deleteStudent(id);
                response.setStatus(SUCCESS.getDisplayName());
                return new ResponseEntity<APIResponse>(response, HttpStatus.OK);
            } catch (ServiceException e) {
                response.setStatus(FAILURE.getDisplayName());
                throw new ServiceException("NOT DELETED", e);
            }
        }
         else {
            System.out.println("invalid user");
            response.setStatus(FAILURE.getDisplayName());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
