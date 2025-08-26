package com.coder;


import com.coder.config.FeignConfig;
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
@EnableFeignClients(
        basePackages = "com.coder.client",
        defaultConfiguration = FeignConfig.class
)
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
