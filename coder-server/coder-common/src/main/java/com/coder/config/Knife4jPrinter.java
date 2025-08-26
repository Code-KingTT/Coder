package com.coder.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class Knife4jPrinter implements ApplicationRunner {

    @Resource
    private Environment environment;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String port = environment.getProperty("server.port", "8081");
        String contextPath = environment.getProperty("server.servlet.context-path", "");

        log.info("Knife4j Doc: \thttp://localhost:{}{}/doc.html", port, contextPath);
    }
}
