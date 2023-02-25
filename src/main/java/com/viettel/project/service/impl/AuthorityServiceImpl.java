package com.viettel.project.service.impl;

import com.viettel.project.entity.Authority;
import com.viettel.project.repository.AuthorityRepository;
import com.viettel.project.service.AuthorityService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    public AuthorityServiceImpl(AuthorityRepository authorityRepository){
        this.authorityRepository = authorityRepository;
    }

    private final AuthorityRepository authorityRepository;

    @Transactional
    @Override
    public void saveAuthority(String path, String methodName, String roleUser) {
        Authority authority = new Authority();
        authority.setAuthority(roleUser);
        authority.setPath(path);
        authority.setStatus(1);
        authority.setMethodName(methodName);

        authority.setCreatedDate(new Date());
        User user = getUser();
        authority.setCreateBy(user.getUsername());
        authority.setLastModifiedBy(user.getUsername());
        authorityRepository.save(authority);
    }

    @Override
    public void deleteAll() {
        authorityRepository.deleteAll();
    }

    @Override
    public Authority findByPathAndMethodAndStatus(String path, String method) {
        return authorityRepository.findByMethodNameAndPathAndStatus(method, path, 1).orElse(null);
    }

    private User getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return user;
    }
}
