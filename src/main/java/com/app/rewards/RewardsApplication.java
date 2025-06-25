package com.app.rewards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class RewardsApplication {

    public static void main(String[] args) {
        SpringApplication.run(RewardsApplication.class, args);
    }

}
