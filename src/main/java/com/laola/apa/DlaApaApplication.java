package com.laola.apa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.laola.apa.mapper")
public class DlaApaApplication {
    public static void main(String[] args) {
        SpringApplication.run(DlaApaApplication.class, args);
    }
}
