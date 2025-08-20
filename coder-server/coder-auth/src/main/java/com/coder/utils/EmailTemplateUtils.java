package com.coder.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 邮件模板工具类
 *
 * @author Sunset
 * @date 2025-8-20
 */
@Slf4j
@Component
public class EmailTemplateUtils {

    /**
     * 读取并渲染邮件模板
     *
     * @param templatePath 模板文件路径
     * @param variables    变量映射
     * @return 渲染后的HTML内容
     */
    public String renderTemplate(String templatePath, Map<String, String> variables) {
        try {
            // 读取模板文件
            String template = readTemplateFile(templatePath);
            
            // 替换变量
            return replaceVariables(template, variables);
            
        } catch (Exception e) {
            log.error("渲染邮件模板失败：{}", e.getMessage(), e);
            return getFallbackContent(variables);
        }
    }

    /**
     * 读取模板文件内容
     */
    private String readTemplateFile(String templatePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(templatePath);
        StringBuilder content = new StringBuilder();
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        
        return content.toString();
    }

    /**
     * 替换模板变量
     */
    private String replaceVariables(String template, Map<String, String> variables) {
        String result = template;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            String placeholder = "${" + entry.getKey() + "}";
            result = result.replace(placeholder, entry.getValue());
        }
        return result;
    }

    /**
     * 获取备用内容（模板加载失败时使用）
     */
    private String getFallbackContent(Map<String, String> variables) {
        return String.format(
                "<div style='padding: 20px; font-family: Arial, sans-serif;'>" +
                "<h2>%s</h2>" +
                "<p>您的验证码是：<strong style='font-size: 24px; color: #e74c3c;'>%s</strong></p>" +
                "<p>验证码有效期为5分钟，请及时使用。</p>" +
                "<p>Coder 团队</p>" +
                "</div>",
                variables.getOrDefault("welcomeText", "验证码"),
                variables.getOrDefault("code", "000000")
        );
    }
}