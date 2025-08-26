package com.coder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication(exclude = {WebMvcAutoConfiguration.class})
@EnableDiscoveryClient
@ComponentScan(excludeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.coder\\.config\\.WebConfig"),
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.coder\\.interceptor\\..*"),
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.coder\\.exception\\.GlobalExceptionHandler")
})
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(GatewayApplication.class);
        app.setWebApplicationType(WebApplicationType.REACTIVE);
        app.run(args);
    }
}