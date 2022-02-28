package com.example.util;

import com.example.dao.impl.AdminAccessDaoImpl;
import com.example.dao.impl.StaffManagementDaoImpl;
import com.example.dao.impl.StudentManagementDaoImpl;
import com.example.dto.AdminDto;
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

    private static final long serialVersionUID = -2550185165626007488L;

    public static final long JWT_TOKEN_VALIDITY = 5*60*60;

    private String secret = "54w65t5$$#$";

    public String getUsernameFromToken(String token) {
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

    public String generateToken(StaffDto staffDto) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, staffDto.getUsername());
    }

    public String generateToken(AdminDto adminDto) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, adminDto.getUsername());
    }

    public String generateToken(StudentDto studentDto) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, studentDto.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY*1000)).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public Boolean canTokenBeRefreshed(String token) {
        return (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    public Boolean validateStaffToken(String token) {
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
}
