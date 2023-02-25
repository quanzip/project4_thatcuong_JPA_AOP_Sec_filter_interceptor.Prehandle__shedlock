package com.viettel.project.entity;

import javax.persistence.*;

@Entity
@Table(name = "authority")
public class Authority extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String path;
    private String authority;
    private Integer status;
    private String methodName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String toString() {
        return "Authority{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", authority='" + authority + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
