package com.B305.ogym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class OgymApplication {

    public static void main(String[] args) {
        SpringApplication.run(OgymApplication.class, args);
    }

}
