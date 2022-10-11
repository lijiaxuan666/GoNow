package com.balls.maintest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@SpringBootApplication
@ComponentScan("com.balls")
@MapperScan("com.balls.mapper")
public class MainTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainTestApplication.class, args);
    }

}
