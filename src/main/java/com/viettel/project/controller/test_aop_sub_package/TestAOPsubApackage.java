package com.viettel.project.controller.test_aop_sub_package;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TestAOPsubApackage {
    private Logger logger = LoggerFactory.getLogger(TestAOPsubApackage.class);
    public void print(){
        logger.info("Running test sub package of controller");
    }
}
