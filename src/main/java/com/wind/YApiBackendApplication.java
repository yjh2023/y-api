package com.wind;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 入口类
 *
 * @author wind
 */
@SpringBootApplication
@MapperScan("com.wind.mapper")
public class YApiBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(YApiBackendApplication.class, args);
    }

}
