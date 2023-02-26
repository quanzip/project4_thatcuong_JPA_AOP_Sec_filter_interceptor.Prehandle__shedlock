package com.viettel.project.security;

import com.viettel.project.entity.User;
import com.viettel.project.service.JwtService;
import com.viettel.project.service.UserService;
import com.viettel.project.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/*
 * This class is for checking every single request to app,
 * wether they are valid with token that they are using.
 *
 * IMportant:
 * Filter se chan cac request tu client de kiem tra truoc khi chung den dc vs cac API
 * */

/*We can combine basic authen with jwt in this class to authen, but,
 for basic authen we can use mechanism of spring by adding http.....and.basicAuth(); to method configure(HttpSecurity http)
* instead of customizing here, i just do it for example*/

/* One more way to authen: API key, typically, it is use for authen among services in system, not client-server
 * link: https://stackoverflow.com/questions/48446708/securing-spring-boot-api-with-api-key-and-secret
 *
 * */

@Configuration
//@Component
public class SecurityFilter extends OncePerRequestFilter {
    private String bearerPrefix = "Bearer ";
    private String basicAuthPrefix = "Basic ";

    @Value(value = "${security.path.ignored:no path}")
    public String ignoredPath;

    @Value(value = "${security.api-key}")
    public String apiKeyValue;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();
        if (!ignoredPath.contains(path)) {
            String authHeader = request.getHeader("authorization");
            try {
                if (authHeader.contains(basicAuthPrefix)) {
                    getUserNameFromBasicAuthenAndValidateAuthentication(request);
                /*
                Authen by using another way!!
                 - currently not using bearer token to authen, consider using basic authen, api key authen...*/
//                filterChain.doFilter(request, response);
                } else if(authHeader.contains(bearerPrefix)) {
                    String token = getTokenFromRequest(request);
                    /*
                     now re-validate token to see if it is legel, non-expired or not.
                        get token from request: header contain authorization
                     */
                    String userName = jwtService.getUserNameFromToken(token);

                    // get authentication from token(token contain userName, use loadUserbyUserName)
                    Authentication authentication = jwtService.getAuthenticationFromUserName(userName);
                    if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
                        // set valid authentication to security CONTEXT
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        logger.info("AUthenticated using basic auth by: Bearer token of JWT");
                    }
                }else {
                    /* in case using API-key to authen among services */
                    int apiKeyIndex = authHeader.indexOf(apiKeyValue);
                    if(apiKeyIndex >= 0){
                        ApiKeyAuthenticationToken token = new ApiKeyAuthenticationToken(apiKeyValue, Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
                        token.setAuthenticated(true);
                        SecurityContextHolder.getContext().setAuthentication(token);
                        logger.info("Authenticated successfuly using API-KEY method.");
                    }else {
                        response.sendError(401, "Request has no authen to execute method using api-key");
                    }
                }
            } catch (Exception ex) {
                response.sendError(401, ex.getMessage());
                throw ex;
            }
        }
        // isgoing
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) throws AccessDeniedException {
        // token os store im the header of the request.
        String authenHeader = request.getHeader("authorization");
        if (!Objects.isNull(authenHeader) && authenHeader.startsWith("Bearer ")) {
            return authenHeader.substring(7);
        } else {
            return "";
        }
//        throw new AccessDeniedException("Request does not contain authorization header");
    }

    private String getUserNameFromBasicAuthenAndValidateAuthentication(HttpServletRequest request) {
        /* This is the way to get value base64 of basic authentication: username:password */
        String authenValue = request.getHeader("authorization");
        System.out.println("Authenvalue: " + authenValue);
        if (!Objects.isNull(authenValue) && !authenValue.isEmpty()) {

            int basicIndex = authenValue.indexOf("Basic ");
            if (basicIndex >= 0) {
                String credential = authenValue.substring("Basic ".length());
                String encodeCredential = new String(Base64.getDecoder().decode(credential), StandardCharsets.UTF_8);

                String credentialParts[] = encodeCredential.split(":");
                String userName = credentialParts[0];

                try {
                    // authenticate userName: password from basicAuth value using spring method: authenticate
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(userName, credentialParts[1]));
                    User user = userMapper.toEntity(userService.findByUserName(userName));

                    /* set simple role for authentication */
                    List<SimpleGrantedAuthority> simpleAuth = user.getRoles().stream()
                            .filter(role -> !Objects.isNull(role.getRole()))
                            .map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());

                    Authentication authentication = new UsernamePasswordAuthenticationToken(userName, "", simpleAuth);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.info("AUthenticated using basic auth by: FILTER");
                } catch (Exception ex) {
                    SecurityContextHolder.clearContext();
                }

                return credential;
            } else {
                return "";
            }
        } else {
            return "";
        }

    }
}
