package com.vfsglobal.utilities;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class UtilitiesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UtilitiesServiceApplication.class, args);
    }
}

