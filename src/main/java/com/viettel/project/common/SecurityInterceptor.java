package com.viettel.project.common;

import com.viettel.project.entity.Authority;
import com.viettel.project.service.AuthorityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// use for check role or distictwish tenent(tap khach hang)
// or check security for app

// filter is for checking token validation

/*
 * y tuong: lay tat ca cac request gui len, phan loaji xem no bat dau o dau
 * sau do tta cos the if-else
 *
 * chung ta se tao 1 bang: luu duong dan va quyen tuong ung vs duong dan do, vs Authority(id, path, role, status)
 * va check xem duong dan hien tai co quyen khop vs ban ghi nao trong bang Authority tren hay khong
 * Neu co, no la hojp le, neu khong co, vang loi thieu quyen
 *
 * - Neu chung ta chi chung check quyen tren moi method(@PreAuthorize(hasRole(X))) thi qua nhieu, mat cong, maintain kho
 * - Neu chung ta chi check role qua class Security chinh: AppSecurity thi hoi cung nhac, chua linh dong
 * ta chi nen config trong class chung: AppSecurity cai khung chung nhat, nhung config chi tiet nen dat tai day
 * - Nen chung ta can config them o day de co the linh dong hon doi vs moi path, maintain cung de dang hon
 *  nhieu noi dang ap dung
 *
 *  Interceptor se chan cac request de kiem tra role cua user co map vs role cua API dang goi
 *  Ket qua tra ve la true | false cung kh quyet dinh duoc user cos dc authen / authorize hay khong
 *  nen van de authen / authorization
 * */

@Component
public class SecurityInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);

    @Value(value = "${security.path.ignored}")
    protected String ignoredPaths;

    @Autowired
    private AuthorityService authorityService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        logger.info("Running through pre-handle");

        String path = request.getServletPath();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        logger.info("request uri: " + uri + ", path: " + path + ", method: " + method);

        List<String> ignores = Arrays.asList(ignoredPaths.split(","));
        if (ignores.contains(path)) return true;
//      isgoing

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Authority authority = authorityService.findByPathAndMethodAndStatus(path, method);
//        This role is now single: ROLE_ADMIN, but in the future, it can pe multiple like : [ROLE_ADMIN,ROLE_X,AUTH_Y]
//        We have to handle that case

//        in case of api has not been configed in database table: authority.
        if (Objects.isNull(authority)) {
            if (!isAuthenticated(authentication)) {
                throw new AccessDeniedException("You need to be authenticated to access this function");
            }
            return true;
        } else {
            String roleOfCurrentPath = authority.getAuthority();
            // we should use JAVA REGEX to compare path in this case,
            // vd if path.matches("/user/**"), equal can not solve this case
            if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
//                Get role of current logined user
                List<String> roles = authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
                if (!roles.contains(roleOfCurrentPath))
                    throw new AccessDeniedException("You need to have role coresponding to access this function" + path + ", method: " + method + ", require role: " + roleOfCurrentPath + ", role user: " + roles);
            } else {
                throw new AccessDeniedException("You need to have role coresponding to access this function: " + path + ", method: " + method + ", require role: " + roleOfCurrentPath + ", user is not authenticated");
            }
        }

        return true; // mean request is valid to run
//        false mean request is not enough roles to perform continue
    }

    private boolean isAuthenticated(Authentication authentication) {
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        logger.info("Running through post-handle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("Running through after completion");
    }


}
