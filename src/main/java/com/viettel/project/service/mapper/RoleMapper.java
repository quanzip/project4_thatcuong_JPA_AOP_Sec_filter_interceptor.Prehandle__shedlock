package com.viettel.project.service.mapper;

import com.viettel.project.entity.Role;
import com.viettel.project.service.dto.RoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Objects;

@Mapper(componentModel = "spring", uses = {})
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {
    @Mapping(source = "user.id", target = "userId")
    RoleDTO toDTO(Role role);

    @Mapping(source = "userId", target = "user.id")
    Role toEntity(RoleDTO roleDTO);

    default Role fromId(Long id) {
        if (Objects.isNull(id)) return null;
        else {
            Role role = new Role();
            role.setId(id);
            return role;
        }
    }
}
