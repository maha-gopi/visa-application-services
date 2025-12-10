package com.vfsglobal.valueaddedservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class ValueAddedServicesServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ValueAddedServicesServiceApplication.class, args);
    }
}

