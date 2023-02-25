package com.viettel.project.service.impl;

import com.viettel.project.common.AppProperty;
import com.viettel.project.common.FileUtils;
import com.viettel.project.entity.Role;
import com.viettel.project.entity.User;
import com.viettel.project.repository.UserRepository;
import com.viettel.project.service.RoleService;
import com.viettel.project.service.UserService;
import com.viettel.project.service.dto.RoleDTO;
import com.viettel.project.service.dto.UserDTO;
import com.viettel.project.service.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private AppProperty appProperty;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserDTO> msearchUser(String search, Integer page, Integer size) {
        logger.info("Getting users...");
        Pageable pageable = PageRequest.of(Objects.isNull(page) ? 0 : page, Objects.isNull(size) ? 10 : size);
        List<UserDTO> res;
        if (Objects.isNull(search) || search.isEmpty()) {
            Page<User> users = userRepository.findAll(pageable);
            res = userMapper.toDTOS(users.getContent());
        } else {
            Page<User> users = userRepository.searchUser("%" + search + "%", pageable);
            res = userMapper.toDTOS(users.getContent());
        }
        return res;
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NoResultException("Could not found user by Id: " + id));
        return userMapper.toDTO(user);
    }

    @Override
    public void addUser(UserDTO userDTO) {
        if (Objects.isNull(userDTO)) throw new NoResultException("Null user to save");
        logger.info("Adding user: " + userDTO.getUserName());

        // save user info
        String password = userDTO.getPassword();
        if (password == null) {
//          using password encoder to encode password for security
            password = "123";
        }
        userDTO.setPassword(passwordEncoder.encode(password));

        // save avatar for user to folder
        MultipartFile file = userDTO.getFile();
        String avatar = "";
        if (file != null && file.getOriginalFilename() != null && !file.getOriginalFilename().isEmpty()) {
            avatar = FileUtils.genNewFileNameFromFile(file);
            File avatarFile = new File(appProperty.getUserImageFolder() + "/" + avatar);
            FileUtils.saveFileToFolder(file, avatarFile);
        }
        userDTO.setAvatar(avatar);
        User user = userMapper.toEntity(userDTO);
        userRepository.save(user);

        // save user roles
        List<RoleDTO> roleDTOS = userDTO.getRoleDTOS();
        if (roleDTOS != null && !roleDTOS.isEmpty()) {
            roleService.saveUserRoles(roleDTOS, user.getId());
        }
    }

    @Override
    public void downloadUserAvatar(Long userId, HttpServletResponse httpServletResponse) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoResultException("Can not found user by id: " + userId));
        String userAvatar = user.getAvatar();
        if (userAvatar != null && !userAvatar.isEmpty()) {
            File file = new File(appProperty.getUserImageFolder() + "/" + userAvatar);
            if (file.exists()) {
                httpServletResponse.setContentType("application/octet-stream");
                try {
                    FileCopyUtils.copy(FileCopyUtils.copyToByteArray(file),
                            httpServletResponse.getOutputStream());  // c1
//                    Files.copy(file.toPath(), httpServletResponse.getOutputStream()); // c2
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void deleteUserById(Long id) {
        logger.info("Deleting user by id: " + id);
        // delete user
        User user = userRepository.findById(id).orElseThrow(() -> new NoResultException("Can not found user by id: " + id));
        userRepository.delete(user);

        // delete user avatar file
        String userAvatar = user.getAvatar();
        if (!Objects.isNull(userAvatar) && !userAvatar.isEmpty()) {
            File file = new File(appProperty.getUserImageFolder() + "/" + userAvatar);
            if (file.exists()) {
                logger.info("Deleting image of user by id: " + id + ", result: " + file.delete());
            }
        }
    }

    @Override
    public void updateUser(UserDTO userDTO) {
        if (Objects.isNull(userDTO)) throw new NoResultException("could not update null userDTO");
        // delete old role, update new roles
        Long userId = userDTO.getId();
        User user = userRepository.findById(userId).orElseThrow(() -> new NoResultException("User does not exist!"));
        String oldAvatar = user.getAvatar();

        List<Role> oldRoles = user.getRoles();
        if (!oldRoles.isEmpty()) {
            roleService.deleteRoles(oldRoles);
        }

        List<RoleDTO> newRoles = userDTO.getRoleDTOS();
        if (!newRoles.isEmpty()) {
            roleService.saveUserRoles(newRoles, userId);
        }

        // delete old avatar and update new avatar
        String newAvatar = null;
        MultipartFile file = userDTO.getFile();
        if (!Objects.isNull(file) && file.getOriginalFilename() != null && !file.getOriginalFilename().isEmpty()) {
            // save new avatar
            newAvatar = FileUtils.genNewFileNameFromFile(file);
            File avatarFile = new File(appProperty.getUserImageFolder() + "/" + newAvatar);
            FileUtils.saveFileToFolder(file, avatarFile);
            userDTO.setAvatar(newAvatar);
        }

        userRepository.save(userMapper.toEntity(userDTO));

        // delete old avatar
        if (!Objects.isNull(oldAvatar)) {
            File oldAvatarFile = new File(appProperty.getUserImageFolder() + "/" + oldAvatar);
            FileUtils.deleteFile(oldAvatarFile);
        }
    }

    @Override
    public Optional<UserDTO> findUserById(Long userId) {
        return userRepository.findById(userId).map(user -> userMapper.toDTO(user));
    }

    @Override
    public UserDTO updateUserPassWord(Long userId, String passW) throws Exception {
        if (passW.length() < 8)
            throw new Exception("User password is short and pridictable, it mast be longer than 7 letters");
//        String encodedPassword = encode password
        User user = userRepository.findById(userId).orElseThrow(() -> new NoResultException("Not found user by id: " + userId));
        user.setPassword(passwordEncoder.encode(passW));
        userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @Transactional
    @Override
    public UserDTO findByUserName(String username) {
        User user = userRepository.findByLogin(username)
                .orElseThrow(() -> new NoResultException("Could not found user by userName: " + username));
        return userMapper.toDTO(user);
    }
}
