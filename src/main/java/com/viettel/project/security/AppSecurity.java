package com.viettel.project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
// de ho tro cau hinh phan quyen tren cac phuong thuc
public class AppSecurity extends WebSecurityConfigurerAdapter {

    // 1: Xac thuc: authentication : google, facebook,...
    // 2: Phan quyen: authorization: phan quyen, co the trn tung ban ghi(it dung), tren tung ham(it dung),
    // va tren tung duong dan (pho bien)
    // 3: có thể chặn truy cập với những user không đủ quyền bằng các cách sau:
//            1: config trong class security chính với mỗi API
//            2: Sử dụng APO: @aspect với @Befỏre, @After để check quyền với mỗi method hoặc mỗi controller
//            3: sử dụng Filter

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // this method is for authentication, facebook, google,... then we have to config this method
        // this project i only use mariadb to authen user
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    /*
     * hasRole: dung khi user co cac role theo format: ROLE_ADMIN, ROLE_USER (format mac dinh cau spring)
     * hasAuthority: dung khi user co cac role khong theo format cuar spring, co the la bat cu gi. cd roleX, Write, Read,...
     * */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /*
        * CSRF is not nessessary for back-end springboot app. so we dont have to prevent it by adding: http.csrf().disable()
        * if we dont add, we can not use use any POST method in the app when app is using security (require username/password),
        * , we can only use get method require username/password.
        *
        * so remember to disable this to call any POST method require username/password.
        * */

        http.csrf().disable().authorizeRequests()

//  Th dung hasAuthority khi role  KHONG DAT THEO QUY TAC CUA SPRING
                .antMatchers("/user/**").permitAll()
                .antMatchers("/user/delete").hasAuthority("ROLE_ADMIN")

                .antMatchers("/product").hasRole("ADMIN") // chi can co quyen: anyAutho
                .antMatchers("/bill", "/billitem").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")


// NHUNG Th de role theo QUY TAC CUA SPRING: ROLE_X, ROLE_Y thi nen dung theo kieu nay:
                .antMatchers("/example", "/example1").hasAnyRole("ADMIN") // can co role: ROLE_ADMIN
                .antMatchers("/real", "/example2").hasRole("USER") // can co role: ROLE_USER

                // must Have role: ROLE_ADMIN
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/swagger-ui/**", "/sw", "/home").permitAll()
                .antMatchers( HttpMethod.POST, "/login-custom").permitAll()
                .anyRequest().authenticated()

                .and()
                .formLogin()
//                .loginPage("")
//                .defaultSuccessUrl("/sw")
                .permitAll();
//
//                .and()
//                .httpBasic();
    }
}
