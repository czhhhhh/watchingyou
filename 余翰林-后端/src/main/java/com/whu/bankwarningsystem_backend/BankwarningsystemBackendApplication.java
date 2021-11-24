package com.whu.bankwarningsystem_backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.whu.bankwarningsystem_backend.*.mapper")
@EnableScheduling
@EnableAsync
public class BankwarningsystemBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankwarningsystemBackendApplication.class, args);
    }

}
