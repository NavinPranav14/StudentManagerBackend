package com.example.controller;

import com.example.controller.response.APIResponse;
import com.example.dto.OdApplyDto;
import com.example.service.MailService;
import com.example.service.OdApplyService;
import com.example.util.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.enums.APIResponseStatus.SUCCESS;

@RestController
@RequestMapping("/od")
public class OdApplyController {

    @Autowired
    private MailService mailService;

    @Autowired
    private OdApplyService odApplyService;

    @Autowired
    private JwtUtility jwtUtility;

    @GetMapping("/all")
    public ResponseEntity<APIResponse> getAllOD (){
        APIResponse response = new APIResponse();
        response.setStatus(SUCCESS.getDisplayName());
        response.setMessage("Login success");
        response.setData(odApplyService.getall());

        return new ResponseEntity<APIResponse>(response, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<APIResponse> getOdId (@PathVariable String id){
        APIResponse response = new APIResponse();
        response.setStatus(SUCCESS.getDisplayName());
        response.setMessage("Login success");
        response.setData(odApplyService.getById(id));
        return new ResponseEntity<APIResponse>(response, HttpStatus.OK);
    }


    @PostMapping("/apply")
    public ResponseEntity<APIResponse> applyOD (@RequestBody OdApplyDto odApplyDto){
        odApplyService.odApply(odApplyDto);
        APIResponse response = new APIResponse();
        response.setStatus(SUCCESS.getDisplayName());
        response.setMessage("Login success");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("jwttoken",jwtUtility.generateToken(odApplyDto, 10*60*60));
        return new ResponseEntity<APIResponse>(response,httpHeaders, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<APIResponse> UpdateOD (@PathVariable String id, @RequestBody OdApplyDto odApplyDto){
        odApplyService.updateStatus(id, odApplyDto);
        APIResponse response = new APIResponse();
        response.setStatus(SUCCESS.getDisplayName());
        response.setMessage("Login success");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("jwttoken",jwtUtility.generateToken(odApplyDto, 10*60*60));
        return new ResponseEntity<APIResponse>(response,httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/email")
    public void odEmailService(@RequestBody OdApplyDto odApplyDto){
        mailService.odEmailService(odApplyDto);
    }
}
