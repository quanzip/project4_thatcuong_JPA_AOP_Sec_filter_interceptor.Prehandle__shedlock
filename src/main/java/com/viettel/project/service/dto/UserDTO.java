package com.viettel.project.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public class UserDTO extends BaseEntityDTO {
    private Long id;
    @Size(min = 6)
    private String userName;
    @Size(min = 6)
    private String login;
    @Size(min = 3, max = 10)
    private String password;

    @DateTimeFormat(pattern = "dd/MM/yyyy") // use for @ModelAttribute
    // when FE submit form. data will be format like this to pass to BE

    @JsonFormat(pattern = "dd/MM/yyyy") // use for @RequestBody,
    // but this DTO has file, so it must use @ModelAttribute
    // when Api called, BE return to FE, data will be formated to this format too
    private LocalDate birthDate;
    private String avatar;
    private String email;
    private List<RoleDTO> roleDTOS;

    // @Transient use this wwhen we want this property does not appear in table but till in Object
    @JsonIgnore // Khi tra ra data cho FE, se bo qua data cua field nay
    private MultipartFile file;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<RoleDTO> getRoleDTOS() {
        return roleDTOS;
    }

    public void setRoleDTOS(List<RoleDTO> roleDTOS) {
        this.roleDTOS = roleDTOS;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", birthDate=" + birthDate +
                ", avatar='" + avatar + '\'' +
                ", email='" + email + '\'' +
                ", roleDTOS=" + roleDTOS +
                ", file=" + file +
                '}';
    }
}
