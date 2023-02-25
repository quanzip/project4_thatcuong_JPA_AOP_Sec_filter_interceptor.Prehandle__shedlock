package com.viettel.project.security;

import com.viettel.project.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Objects;

/*
 * This class is for checking every single request to app,
 * wether they are valid with token that they are using.
 *
 * IMportant:
 * Filter se chan cac request tu client de kiem tra truoc khi chung den dc vs cac API
 * */

//@Configuration
@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
//           now re-validate token to see if it is legel, non-expired or not.
//           get token from request: header contain authorization
            String token = getTokenFromRequest(request);
            String userName = jwtService.getUserNameFromToken(token);

            // get authentication from token(token contain userName, use loadUserbyUserName)
            Authentication authentication = jwtService.getAuthenticationFromUserName(userName);
            if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {

                // set valid authentication to security CONTEXT
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            // token of this request is invalid, xoa thong tin
            SecurityContextHolder.clearContext();
//            vang ra loi chua dang nhap, vi token truyen vao khong con hop le
            response.sendError(401, ex.getMessage());
            throw ex;
        }
        // isgoing
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) throws AccessDeniedException {
        // token os store im the header of the request.
        String authenHeader = request.getHeader("authentication");
        if (!Objects.isNull(authenHeader) && authenHeader.startsWith("Bearer ")) {
            return authenHeader.substring(7);
        }
        throw new AccessDeniedException("Request does not contain authorization header");
    }
}
