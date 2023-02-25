package com.viettel.project.common;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.authentication.AuthenticationManagerFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

/*
* Voi cac bean da tao nay, chung ta co the goi chung ra tu bat ky dau vs @AutoWire
* neu khong muon @AutoWire bean X, ta co the dung cach sau:
* @Autowire
* private ApplicationContext applicationContext;
*
* void doLogic(){  Bean x = applicationContext.getBean(X.class)  }
*
* */

@Configuration
public class Beans {
    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LockProvider getLockProvider(DataSource dataSource){
        return new JdbcTemplateLockProvider(dataSource);
    }

    @Bean
    public NewTopic getNewTopic(){
        return new NewTopic("del-billitem", 3, (short)1);
    }

    @Bean
    public JsonMessageConverter getJsonMessageConverter(){
        return new JsonMessageConverter();
    }

    @Bean
    public AuthenticationManager getAuthenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
