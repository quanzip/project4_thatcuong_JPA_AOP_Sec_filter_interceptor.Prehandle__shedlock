package com.viettel.project.service.mapper;

import com.viettel.project.entity.User;
import com.viettel.project.service.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Objects;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper extends EntityMapper<UserDTO, User> {
    @Mapping(source = "roles", target = "roleDTOS")
    UserDTO toDTO(User role);

    @Mapping(source = "roleDTOS", target = "roles")
    User toEntity(UserDTO roleDTO);

    default User fromId(Long id) {
        if (Objects.isNull(id)) return null;
        else {
            User user = new User();
            user.setId(id);
            return user;
        }
    }
}
