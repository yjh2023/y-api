package com.wind;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 入口类
 *
 * @author wind
 */
@SpringBootApplication
@EnableDubbo
@MapperScan("com.wind.mapper")
public class YApiBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(YApiBackendApplication.class, args);
    }

}
