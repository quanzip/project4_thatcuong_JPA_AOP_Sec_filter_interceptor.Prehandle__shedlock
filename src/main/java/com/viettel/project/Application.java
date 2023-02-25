package com.viettel.project;

import com.google.gson.*;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.AbstractSwaggerUiConfigProperties;
import org.springdoc.webmvc.ui.SwaggerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT4S")
public class Application implements CommandLineRunner {
    private final  Logger logger = LoggerFactory.getLogger(Application.class);
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Application.class);
        final Logger logger = LoggerFactory.getLogger(Application.class);
        ConfigurableApplicationContext context = springApplication.run(args);
        ConfigurableEnvironment environment = context.getEnvironment();
        String appName = environment.getProperty("spring.application.name");
        String port = environment.getProperty("server.port");
        logger.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running!\n" +
                        "\t{}: http://localhost:{}\n" +
                        "----------------------------------------------------------",
                appName, appName, port);
    }

    @Override
    public void run(String... args) throws Exception {
////        X x =  applicationContext.getBean(X.class)
//        try {
//            TestAutoWirePropertiesInNonBean testAutoWirePropertiesInNonBean = (TestAutoWirePropertiesInNonBean)
//                    Class.forName("com.viettel.project.service.dto.TestAutoWirePropertiesInNonBean").newInstance();
//            autowireCapableBeanFactory.autowireBean(testAutoWirePropertiesInNonBean);
//            testAutoWirePropertiesInNonBean.printProperty();
//
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//        }

    }

}
