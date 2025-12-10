package com.vfsglobal.bookings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class BookingsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingsServiceApplication.class, args);
    }
}

