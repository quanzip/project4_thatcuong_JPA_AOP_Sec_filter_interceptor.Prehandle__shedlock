package com.viettel.project.service;

import com.viettel.project.service.dto.UserDTO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDTO> msearchUser(String search, Integer page, Integer size);

    UserDTO getUserById(Long id);

    void addUser(UserDTO userDTO);

    void downloadUserAvatar(Long userId, HttpServletResponse httpServletResponse);

    void deleteUserById(Long id);

    void updateUser(UserDTO userDTO);

    Optional<UserDTO> findUserById(Long userId);

    UserDTO updateUserPassWord(Long userId, String passW) throws Exception;

    UserDTO findByUserName(String username);

}
