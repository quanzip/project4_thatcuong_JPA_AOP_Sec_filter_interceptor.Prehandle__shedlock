package com.viettel.project.service.impl;

import com.viettel.project.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {

    @Value(value = "${jwt.secret-key:123456}")
    private String secretKey;

    private long validityInMiliSecond = 3600000;    // 1h

    @Autowired
    private UserDetailsService userDetailsService;

    //    This token using only userName inside token. we can combine password, role, authority,. ...
//    This is the standart for many system like facebook, google.
    public String generateToken(String userName) {
        // khong duoc sd password cho token.
        // set subject for token.
        Claims claims = Jwts.claims().setSubject(userName);
        // we can add authority to claims by put values to claims:
//        claims.put()
//        claims.putAll();

// algorithm here i can use HS256 or ES256
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8)
                , SignatureAlgorithm.ES256.getJcaName());
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + validityInMiliSecond);
        String token = Jwts.builder()
                .setClaims(claims)
                .setExpiration(expireDate)
                .setIssuedAt(now)
//                Must use hs256
//                .signWith(SignatureAlgorithm.HS256, secretKeySpec) //  ok to use => A
                .signWith(SignatureAlgorithm.HS256, secretKey)  // ok to use either
//                .setHeader()
                .compact();
        System.out.println("validate result: " + validateToken(token));
        return token;
    }

    @Override
    public boolean validateToken(String token){
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8),
                SignatureAlgorithm.ES256.getJcaName());
        try{
//            Jwts.parser().setSigningKey(secretKeySpec).parseClaimsJws(token);   // ok to use With A
            // if following parse can run without any problems, then token is valid
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        }catch (Exception ex){
            System.out.println(ex);
            return false;
        }

        return true;
    }


    // we can set roles to claims instead of load roles from DB like below:

    @Override
    public String getUserNameFromToken(String token) throws AccessDeniedException {
        // we can use this to re-set value for body, expire-time, ...
//        Jwts.parser().parseClaimsJws(token).getBody().

//        method:  .parseClaimsJws(token) will check:  is token parseable?
//                                                     is expired-time valid
        // it will also validate token to be valid or not
        try{
//            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject(); // if this can not help,
            // consider using above line to get subject.
        }catch (Exception e){
            throw new AccessDeniedException("Token iinvalid");
        }
    }

    @Override
    public UserDetails getFullUserFromToken(String token) throws AccessDeniedException {
        String userName = getUserNameFromToken(token);
        return userDetailsService.loadUserByUsername(userName);
    }

    @Override
    public Authentication getAuthenticationFromUserName(String username){
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails,
                "",
                userDetails.getAuthorities());
    }
}
