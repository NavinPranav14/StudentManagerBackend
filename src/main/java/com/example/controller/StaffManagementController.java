package com.example.controller;

import com.example.controller.response.APIResponse;
import com.example.dto.StaffDto;
import com.example.exception.NotFoundException;
import com.example.exception.ServiceException;
import com.example.service.AdminService;
import com.example.service.StaffManagementService;
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
@RequestMapping("staff")

public class StaffManagementController {
    @Autowired
    private StaffManagementService staffManagementService;

    @Autowired
    private StudentManagementService studentManagementService;

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private AdminService adminService;

    public Boolean getAdminJwtUtility( String auth) throws NotFoundException {
        return jwtUtility.validateAdminToken(auth) != null;
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse> getStaffAccess(@Valid @RequestBody StaffDto staffDto) throws ServiceException{
        APIResponse response = new APIResponse();
        if(staffManagementService.staffAccess(staffDto)){
            response.setStatus(SUCCESS.getDisplayName());
            response.setMessage("Login success");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("jwttoken",jwtUtility.generateToken(staffDto));
            return new ResponseEntity<APIResponse>(response,httpHeaders, HttpStatus.OK);
        }
        else{
            response.setStatus(FAILURE.getDisplayName());
            response.setMessage("Login failed");
            return new ResponseEntity<APIResponse>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<APIResponse> listStaff() throws ServiceException {
        APIResponse response = new APIResponse();
            response.setData(staffManagementService.listStaff());
            response.setStatus(SUCCESS.getDisplayName());
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> findStaffById(@PathVariable String id)
            throws ServiceException {
        APIResponse response = new APIResponse();
        if(staffManagementService.findStaffById(id) != null){
            response.setData(staffManagementService.findStaffById(id));
            response.setStatus(SUCCESS.getDisplayName());
            return new ResponseEntity<>(response, HttpStatus.OK);

        }
        else{
            response.setStatus(FAILURE.getDisplayName());
            response.setMessage("Id not found");
            return new ResponseEntity<APIResponse>(response, HttpStatus.OK);
        }
        }

    @PostMapping("/create")
    public ResponseEntity<APIResponse> addStaff(@RequestHeader(value = "authorization") String auth,@Valid @RequestBody StaffDto staffDto) throws ServiceException, NotFoundException {
        APIResponse response = new APIResponse();
        if(getAdminJwtUtility(auth)){
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

    @PutMapping("/edit/{id}")
    public ResponseEntity<APIResponse> modifyStaff(@RequestHeader(value = "authorization") String auth, @PathVariable String id,
                                                     @Valid @RequestBody StaffDto staffDto) throws ServiceException, NotFoundException {
        APIResponse response = new APIResponse();
        if(getAdminJwtUtility(auth)) {
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse> deleteStaff(@RequestHeader(value = "authorization") String auth, @PathVariable String id) throws ServiceException, NotFoundException {
        APIResponse response = new APIResponse();
        if(getAdminJwtUtility(auth)) {
                staffManagementService.deleteStaff(id);
                response.setStatus(SUCCESS.getDisplayName());
                return new ResponseEntity<APIResponse>(response,HttpStatus.OK);
            } else{
                response.setStatus(FAILURE.getDisplayName());
                throw new ServiceException("Access denied");
        }
    }
}
