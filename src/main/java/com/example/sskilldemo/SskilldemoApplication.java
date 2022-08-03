package com.example.sskilldemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.sskilldemo")
@MapperScan("com.example.sskilldemo.mapper")

public class SskilldemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SskilldemoApplication.class, args);
    }

}
