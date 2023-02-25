package com.viettel.project.service.impl;

import com.viettel.project.service.UserService;
import com.viettel.project.service.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class LoginServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // check user exist by userName
        UserDTO userDTO = userService.findByUserName(username);

        // set authorities for user
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        userDTO.getRoleDTOS().stream().filter(roleDTO -> !Objects.isNull(roleDTO.getRole())).peek(roleDTO -> {
            simpleGrantedAuthorities.add(new SimpleGrantedAuthority(roleDTO.getRole()));
        }).collect(Collectors.toList());


//        we just need to do this, spring will do the rest: spring will check password passed from login form
//        and compare with password of userDTO we return below.
        return new User(username, userDTO.getPassword(), simpleGrantedAuthorities);
    }

    public static void main(String[] args) {
        List<String> strinArr = new ArrayList<>();
        List<Integer> numbers = Arrays.asList(1,2,3);
        numbers.stream().peek(n
                -> strinArr.add(n.toString())) // logic
                .close();
        // if use .close() like this, then logic will not run (reach), strinArr is still empty after all
        // instead we have to use .collect(Collectors.toList()) then the logic is reached
        System.out.println(strinArr);
    }
}
