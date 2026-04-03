package com.seehold;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.seehold.mapper")
public class AgentGuardStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgentGuardStarterApplication.class, args);
        System.out.println("Hello AG-Starter!");
    }
}
