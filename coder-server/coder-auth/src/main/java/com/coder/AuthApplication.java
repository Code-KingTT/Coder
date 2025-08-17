package com.coder;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 用户服务启动类
 *
 * @author Sunset
 * @date 2025/8/13
 */
@SpringBootApplication(scanBasePackages = "com.coder")
@EnableDiscoveryClient
@EnableFeignClients
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
        System.out.println("=================================");
        System.out.println("认证服务启动成功！");
        System.out.println("Swagger文档地址: http://localhost:8082/doc.html");
        System.out.println("=================================");
    }
}
