package com.coder.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * Knife4j配置类
 * 
 * @author Sunset
 * @date 2025/1/27
 */
@Configuration
@EnableSwagger2WebMvc
@EnableKnife4j
public class Knife4jConfig {

    /**
     * 默认API分组
     * 
     * @return 默认API分组配置
     */
    @Bean
    public Docket defaultApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Coder系统API")
                .apiInfo(apiInfo("Coder系统API文档", "基于Spring Boot + Spring Cloud的微服务系统API文档"))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.coder.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * API信息
     * 
     * @param title       标题
     * @param description 描述
     * @return API信息
     */
    private ApiInfo apiInfo(String title, String description) {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .version("1.0.0")
                .contact(new Contact("Sunset", "", "sunset@713.com"))
                .build();
    }
}
