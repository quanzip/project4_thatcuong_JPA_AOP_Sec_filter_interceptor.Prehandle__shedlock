package com.viettel.project.service;

import com.viettel.project.entity.Role;
import com.viettel.project.service.dto.RoleDTO;

import java.util.List;

public interface RoleService {
    void saveUserRoles(List<RoleDTO> roleDTOS, Long id);

    void deleteRoles(List<Role> oldRoles);
}
