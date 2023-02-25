package com.viettel.project.controller;

import com.viettel.project.common.HttpStatus;
import com.viettel.project.controller.test_aop_sub_package.TestAOPsubApackage;
import com.viettel.project.service.UserService;
import com.viettel.project.service.dto.ResponseDTO;
import com.viettel.project.service.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserResource {
    @Autowired
    private UserService userService;

//    This is only for AOP test: test class in sub-package run?
    @Autowired
    private TestAOPsubApackage testAOPsubApackage;

    @Autowired
    private MessageSource messageSource;

    @GetMapping(value = "")
    public ResponseDTO<List<UserDTO>> getUser(@RequestParam(value = "search", required = false) String search,
                                              @RequestParam(value = "page", required = false) Integer page,
                                              @RequestParam(value = "size", required = false) Integer size) {
        List<UserDTO> result = userService.msearchUser(search, page, size);
//        testAOPsubApackage.print();
        return new ResponseDTO<List<UserDTO>>().status(HttpStatus.http_ok)
//                .message(messageSource.getMessage("success.ok", null, null))
                .message("Getting all users... done!")
                .data(result);
    }

    @PostMapping(value = "/{id}")
    public ResponseDTO<UserDTO> getUserById(@PathVariable(value = "id") Long id) {
        UserDTO res = userService.getUserById(id);
        return new ResponseDTO<UserDTO>().status(HttpStatus.http_ok).message("OK").data(res);
    }

    @PostMapping()
    public ResponseDTO<Void> addUser(@ModelAttribute @Valid UserDTO userDTO) {
        userService.addUser(userDTO);
        return new ResponseDTO<Void>().status(HttpStatus.http_ok).message("Add user successfully!");
    }

    @GetMapping("/download-avatar")
    public ResponseDTO<Void> downloadAvatar(@RequestParam(value = "user_id") Long userId,
                                            HttpServletResponse httpServletResponse) {
        userService.downloadUserAvatar(userId, httpServletResponse);
        return new ResponseDTO<>().status(HttpStatus.http_ok).message("download user avatar done!");
    }

    @PostMapping(value = "/delete/{id}")
    public ResponseDTO<Long> deleteUserById(@PathVariable(value = "id") Long id) {
        userService.deleteUserById(id);
        return new ResponseDTO<>().status(HttpStatus.http_ok).message("Deleted user by id: " + id).data(id);
    }

    @PostMapping(value = "/update")
    public ResponseDTO<Long> updateUser(@ModelAttribute UserDTO userDTO) {
        userService.updateUser(userDTO);
        return new ResponseDTO<>().status(HttpStatus.http_ok).message("Updated user: " + userDTO.getUserName()).data(userDTO.getId());
    }

    @PostMapping(value = "/update-pw")
    public ResponseDTO<UserDTO> updatePassword(@RequestParam(value = "userId", required = false) Long userId
            , @RequestParam(value = "password", required = false) String passW) throws Exception {
        UserDTO userDTO = userService.updateUserPassWord(userId, passW);
        return  new ResponseDTO<>().status(HttpStatus.http_ok).message("Updated password for user id: " + userId).data(userDTO);
    }
}
