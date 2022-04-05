package com.example.controller;

import com.example.controller.response.APIResponse;
import com.example.dto.StudentDto;
import com.example.exception.NotFoundException;
import com.example.exception.ServiceException;
import com.example.service.StudentManagementService;
import com.example.util.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.enums.APIResponseStatus.FAILURE;
import static com.example.enums.APIResponseStatus.SUCCESS;

@RestController
@RequestMapping("/student")
public class StudentManagementController {

    @Autowired
    private StudentManagementService studentManagementService;
    @Autowired
    private JwtUtility jwtUtility;


    public Boolean getStaffJwtUtility( String auth) throws NotFoundException {
        return jwtUtility.validateAdminToken(auth) != null;
    }

    public Boolean getAdminJwtUtility( String auth) throws NotFoundException {
        return jwtUtility.validateAdminToken(auth) != null;
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse> getStudentAccess(@RequestBody StudentDto studentDto) throws ServiceException {
        APIResponse response = new APIResponse();
        if(studentManagementService.studentAccess(studentDto)){
            System.out.println("inside if");
            response.setStatus(SUCCESS.getDisplayName());
            response.setMessage("Login success");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("jwttoken",jwtUtility.generateToken(studentDto));
            return new ResponseEntity<APIResponse>(response,httpHeaders, HttpStatus.OK);
        }
        else{
            System.out.println("Outside if");
            response.setStatus(FAILURE.getDisplayName());
            response.setMessage("Login failed");
            return new ResponseEntity<APIResponse>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<APIResponse> listStudent()
            throws ServiceException {
        APIResponse response = new APIResponse();
            response.setStatus(SUCCESS.getDisplayName());
            response.setData(studentManagementService.listStudent());
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("name/{name}")
    public ResponseEntity<APIResponse> findStudentByName( @PathVariable String name ) throws ServiceException{
        APIResponse response = new APIResponse();
        if(studentManagementService.findStudentByName(name) != null){
            response.setData(studentManagementService.findStudentByName(name));
            response.setStatus(SUCCESS.getDisplayName());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else{
            response.setStatus(FAILURE.getDisplayName());
            response.setMessage("Id not found");
            return new ResponseEntity<APIResponse>(response, HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> findStudentById(@PathVariable String id)
            throws ServiceException {
        APIResponse response = new APIResponse();
        if(studentManagementService.findStudentById(id) != null) {
            response.setData(studentManagementService.findStudentById(id));
            response.setStatus(SUCCESS.getDisplayName());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else{
            response.setStatus(FAILURE.getDisplayName());
            response.setMessage("Id not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("username/{username}")
    public ResponseEntity<APIResponse> findStudentByUsername( @PathVariable String username ) throws ServiceException{
        APIResponse response = new APIResponse();
        if(studentManagementService.findStudentByUsername(username) != null){
            response.setData(studentManagementService.findStudentByUsername(username));
            response.setStatus(SUCCESS.getDisplayName());
            return new ResponseEntity<>(response, HttpStatus.OK);

        }
        else{
            response.setStatus(FAILURE.getDisplayName());
            response.setMessage("Id not found");
            return new ResponseEntity<APIResponse>(response, HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/create")
    public ResponseEntity<APIResponse> addStudent(@Valid @RequestHeader(value = "authorization") String auth, @RequestBody StudentDto studentDto)
            throws ServiceException, NotFoundException {
        APIResponse response = new APIResponse();
        if(getAdminJwtUtility(auth) || getStaffJwtUtility(auth)) {
            studentManagementService.addStudent(studentDto);
            response.setStatus(SUCCESS.getDisplayName());
            return new ResponseEntity<APIResponse>(response, HttpStatus.OK);
        }
        else{

            response.setStatus(FAILURE.getDisplayName());
            response.setMessage("Access denied");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/edit/{id}")

    public ResponseEntity<APIResponse> modifyStudent(@RequestHeader(value = "authorization") String auth, @PathVariable String id,
                                                     @Valid @RequestBody StudentDto studentDto) throws ServiceException, NotFoundException {
        APIResponse response = new APIResponse();
        if(jwtUtility.validateAdminToken(auth)!=null || jwtUtility.validateStaffToken(auth) != null) {
            studentManagementService.modifyStudent(id, studentDto);
            response.setStatus(SUCCESS.getDisplayName());
            return new ResponseEntity<APIResponse>(response, HttpStatus.OK);
        } else {
            response.setStatus(FAILURE.getDisplayName());
            response.setMessage("Access denied");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse> deleteStudent(@RequestHeader(value = "authorization") String auth, @PathVariable String id) throws ServiceException, NotFoundException {
        APIResponse response = new APIResponse();
        if(jwtUtility.validateAdminToken(auth)!=null || jwtUtility.validateStaffToken(auth) != null) {
                studentManagementService.deleteStudent(id);
                response.setStatus(SUCCESS.getDisplayName());
                return new ResponseEntity<APIResponse>(response, HttpStatus.OK);
        }
         else {
            response.setStatus(FAILURE.getDisplayName());
            response.setMessage("Access denied");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
