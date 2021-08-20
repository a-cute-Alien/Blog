package com.wzc.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.wzc.blog.util")
@MapperScan("com.wzc.blog.mapper")
public class BlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }
}
