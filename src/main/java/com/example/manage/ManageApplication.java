package com.example.manage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@ComponentScan(basePackages = "com.example.tombstone")

//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@MapperScan("com.example.manage.api.dao")
@SpringBootApplication
public class ManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManageApplication.class, args);
    }

}
