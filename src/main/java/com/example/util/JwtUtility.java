package com.example.util;

import com.example.dao.OdApplyRespository;
import com.example.dao.impl.AdminAccessDaoImpl;
import com.example.dao.impl.StaffManagementDaoImpl;
import com.example.dao.impl.StudentManagementDaoImpl;
import com.example.dto.AdminDto;
import com.example.dto.OdApplyDto;
import com.example.dto.StaffDto;
import com.example.dto.StudentDto;
import com.example.exception.NotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtility implements Serializable {

    @Autowired
    private StaffManagementDaoImpl staffManagementDao;
    @Autowired
    private StudentManagementDaoImpl studentManagementDao;

    @Autowired
    private AdminAccessDaoImpl adminAccessDao;

    @Autowired
    private OdApplyRespository odApplyRespository;

    private static final long serialVersionUID = -2550185165626007488L;

    public static final long JWT_TOKEN_VALIDITY = 5*60*60;

    private String secret = "54w65t5$$#$";

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String getMentorFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }


    private Boolean ignoreTokenExpiration(String token) {
        return false;
    }

    public String generateToken(StaffDto staffDto, long validity) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, staffDto.getUsername(), validity);
    }


    public String generateToken(String username, long validity) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, username, validity);
    }

    //newly included
    public String generateToken(OdApplyDto odApplyDto, long validity) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, odApplyDto.getMentor(), validity);
    }

    public String generateToken(AdminDto adminDto, long validity) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, adminDto.getUsername(), validity);
    }

    public String generateToken(StudentDto studentDto, long validity) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, studentDto.getUsername(), validity);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject, long validity) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity*1000)).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public Boolean canTokenBeRefreshed(String token) {
        return (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    public Boolean validateStaffToken(String token) {
        final String username = getUsernameFromToken(token);
        return (staffManagementDao.findStaffByUserName(username)!=null && !isTokenExpired(token));
    }


    public Boolean validateResetStaffPassword(String token) {
        final String username = getUsernameFromToken(token);
        return (staffManagementDao.findStaffByUserName(username)!=null && !isTokenExpired(token));
    }

    public Boolean validateStudentToken(String token) {
        final String username = getUsernameFromToken(token);
        return (studentManagementDao.findStudentByUserName(username)!=null && !isTokenExpired(token));
    }

    public Boolean validateAdminToken(String token) throws NotFoundException {
        final String username = getUsernameFromToken(token);
        return (adminAccessDao.adminAccess(username)!=null && !isTokenExpired(token));
    }

    //newly added
    public Boolean validateOdApplyToken(String token) {
        final String mentor = getMentorFromToken(token);
        return (odApplyRespository.findByMentor(mentor)!=null && !isTokenExpired(token));
    }
}
