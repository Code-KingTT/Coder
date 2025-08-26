package com.coder.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 文件配置类
 *
 * @author Sunset
 * @date 2025-8-22
 */
@Data
@Component
@ConfigurationProperties(prefix = "file")
public class FileConfig {

    private Upload upload = new Upload();
    private Access access = new Access();
    private Storage storage = new Storage();

    @Data
    public static class Upload {
        private String basePath = "./uploads/";
        private String tempPath = "./temp/";
        private Long maxFileSize = 10485760L; // 10MB
        private Long maxRequestSize = 52428800L; // 50MB
        private List<String> allowedTypes;
        private List<String> forbiddenTypes;
        private Boolean enableTypeCheck = true;
        private Boolean enableContentCheck = true;
    }

    @Data
    public static class Access {
        private String domain = "http://localhost:8083";
        private String pathPrefix = "/files";
        private String staticPath = "/static/**";
        private Boolean enableAuth = true;
        private List<String> anonymousTypes;
    }

    @Data
    public static class Storage {
        private String defaultType = "LOCAL";
        private Local local = new Local();
    }

    @Data
    public static class Local {
        private String rootPath = "./files/";
        private String urlPrefix = "/files";
    }
}