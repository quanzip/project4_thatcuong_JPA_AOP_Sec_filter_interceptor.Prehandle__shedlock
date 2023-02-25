package com.viettel.project.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app", ignoreInvalidFields = true, ignoreUnknownFields = true)
public class AppProperty {
    private String productImageFolder;
    private String userImageFolder;

    public String getProductImageFolder() {
        return productImageFolder;
    }

    public void setProductImageFolder(String productImageFolder) {
        this.productImageFolder = productImageFolder;
    }

    public String getUserImageFolder() {
        return userImageFolder;
    }

    public void setUserImageFolder(String userImageFolder) {
        this.userImageFolder = userImageFolder;
    }

    @Override
    public String toString() {
        return "AppProperty{" +
                "productImageFolder='" + productImageFolder + '\'' +
                ", userImageFolder='" + userImageFolder + '\'' +
                '}';
    }
}
