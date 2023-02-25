package com.viettel.project.controller;

import com.viettel.project.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class LoginResource {
    private final Logger logger = LoggerFactory.getLogger(LoginResource.class);

    @GetMapping(value = "/me")
    @Secured(value = "ROLE_ADMIN")
    @PreAuthorize(value = "hasRole(ADMIN)")
    public User getAuthenticationPrincipal(Principal principal){
        return (User) principal;
    }

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping(value = "/login-custom")
    public String login(@RequestParam(value = "username") String userName,
                      @RequestParam(value = "password") String password){
//        if authen success, there is no exception
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        // token khong duoc luu password ben trong
        logger.info("Authentication user-name: " + userName + " successfully!!");
        return jwtService.generateToken(userName);
    }

    @GetMapping(value = "/home")
    public String  goToHomePage(){
        return "You get to home";
    }

}
