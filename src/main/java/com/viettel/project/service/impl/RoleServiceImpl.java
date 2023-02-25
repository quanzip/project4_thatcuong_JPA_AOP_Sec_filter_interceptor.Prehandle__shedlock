package com.viettel.project.service.impl;

import com.viettel.project.entity.Role;
import com.viettel.project.repository.RoleRepository;
import com.viettel.project.service.dto.RoleDTO;
import com.viettel.project.service.mapper.RoleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements com.viettel.project.service.RoleService {
    private final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void saveUserRoles(List<RoleDTO> roleDTOS, Long id) {
        // todo test save roles
        roleDTOS = roleDTOS.stream().peek(roleDTO -> roleDTO.setUserId(id)).collect(Collectors.toList());
        roleRepository.saveAll(roleMapper.toEntities(roleDTOS));
        logger.info("Saved all user roles for userId: " + id);
    }

    @Override
    public void deleteRoles(List<Role> oldRoles) {
        roleRepository.deleteAll(oldRoles);
    }
}
