package com.example.controller;

import com.example.controller.response.APIResponse;
import com.example.dto.StaffDto;
import com.example.exception.ServiceException;
import com.example.service.AdminService;
import com.example.service.StaffManagementService;
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
public class StaffManagementController {

    @Autowired
    private StaffManagementService staffManagementService;

    @Autowired
    private StudentManagementService studentManagementService;


    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private AdminService adminService;

    @PostMapping("/staff/login")
    public ResponseEntity<APIResponse> getStaffAccess(@Valid @RequestBody StaffDto staffDto){
        APIResponse response = new APIResponse();

        if(staffManagementService.staffAccess(staffDto)){
            response.setStatus(SUCCESS.getDisplayName());
            response.setMessage("Login success");
            response.setData(jwtUtility.generateToken(staffDto));
            return new ResponseEntity<APIResponse>(response, HttpStatus.OK);
        }
        else{
            response.setStatus(FAILURE.getDisplayName());
            response.setMessage("Login failed");
            return new ResponseEntity<APIResponse>(response, HttpStatus.OK);
        }

    }

    @GetMapping("/staff/all")
    public ResponseEntity<APIResponse> listStaff() throws ServiceException {
        APIResponse response = new APIResponse();
            response.setStatus(SUCCESS.getDisplayName());
            response.setData(staffManagementService.listStaff());
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/staff/{id}")
    public ResponseEntity<APIResponse> findStaffById(@PathVariable String id)
            throws ServiceException {
        APIResponse response = new APIResponse();
            response.setStatus(SUCCESS.getDisplayName());
            response.setData(staffManagementService.findStaffById(id));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

    @PostMapping("/staff/create")
    public ResponseEntity<APIResponse> addStaff(@RequestHeader(value = "authorization") String auth,@Valid @RequestBody StaffDto staffDto) throws ServiceException {
        APIResponse response = new APIResponse();
        if(jwtUtility.validateAdminToken(auth) != null){

           response.setStatus(SUCCESS.getDisplayName());
           staffManagementService.addStaff(staffDto);
           return new ResponseEntity<APIResponse>(response,HttpStatus.OK);
       }
       else{
           response.setStatus(FAILURE.getDisplayName());
           response.setMessage("Access denied");
           return new ResponseEntity<APIResponse>(response,HttpStatus.OK);
       }
    }

    @PutMapping("/staff/edit/{id}")
    public ResponseEntity<APIResponse> modifyStaff(@RequestHeader(value = "authorization") String auth, @PathVariable String id,
                                                     @Valid @RequestBody StaffDto staffDto){
        APIResponse response = new APIResponse();
        if(jwtUtility.validateAdminToken(auth)!=null) {
            response.setStatus(SUCCESS.getDisplayName());
            staffManagementService.modifyStaff(id, staffDto);
            return new ResponseEntity<APIResponse>(response, HttpStatus.OK);
        }
        else{
            response.setStatus(FAILURE.getDisplayName());
            response.setMessage("Access denied");
            return new ResponseEntity<APIResponse>(response,HttpStatus.OK);
        }
    }

    @DeleteMapping("/staff/delete/{id}")
    public ResponseEntity<APIResponse> deleteStaff(@RequestHeader(value = "authorization") String auth, @PathVariable String id) throws  ServiceException {
        APIResponse response = new APIResponse();
        if(jwtUtility.validateAdminToken(auth)!=null) {
            try {
                staffManagementService.deleteStaff(id);
                response.setStatus(SUCCESS.getDisplayName());
                return new ResponseEntity<APIResponse>(response,HttpStatus.OK);
            } catch (ServiceException e) {
                response.setStatus(FAILURE.getDisplayName());
                throw new ServiceException("NOT DELETED", e);
            }
        }
        else{
            response.setStatus(FAILURE.getDisplayName());
            response.setMessage("Access denied");
            return new ResponseEntity<APIResponse>(response,HttpStatus.OK);
        }

    }

}
