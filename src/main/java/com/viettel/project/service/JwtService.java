package com.viettel.project.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.file.AccessDeniedException;

public interface JwtService {
    String generateToken(String userName);

    boolean validateToken(String token);

    String getUserNameFromToken(String token) throws AccessDeniedException;

    UserDetails getFullUserFromToken(String token) throws AccessDeniedException;

    Authentication getAuthenticationFromUserName(String username);
}
