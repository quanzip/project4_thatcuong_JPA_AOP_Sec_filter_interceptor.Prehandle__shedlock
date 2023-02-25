package com.viettel.project.service;

import com.viettel.project.entity.Authority;

public interface AuthorityService {
    void saveAuthority(String path, String methodName, String roleUser);

    void deleteAll();

    Authority findByPathAndMethodAndStatus(String path, String method);
}
