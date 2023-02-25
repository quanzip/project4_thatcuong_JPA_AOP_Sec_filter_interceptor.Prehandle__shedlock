package com.viettel.project.service.dto;

import com.viettel.project.common.AppProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class TestAutoWirePropertiesInNonBean {
    private final Logger logger = LoggerFactory.getLogger(TestAutoWirePropertiesInNonBean.class);

    @Autowired
    private AppProperty appProperty;

    public void printProperty(){
        logger.info(appProperty.toString());
    }
}
