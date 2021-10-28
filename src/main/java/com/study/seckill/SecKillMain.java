package com.study.seckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author TODO
 * @date 2021/10/18 15:50
 **/
@SpringBootApplication
@MapperScan("com.study.seckill.mapper")
public class SecKillMain {
    public static void main(String[] args) {
        SpringApplication.run(SecKillMain.class,args);
    }
}
