package com.coder.service;

/**
 * 邮件服务接口
 *
 * @author Sunset
 * @date 2025-8-17
 */
public interface EmailService {

    /**
     * 发送简单文本邮件
     *
     * @param to      收件人邮箱
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    void sendSimpleEmail(String to, String subject, String content);

    /**
     * 发送HTML邮件
     *
     * @param to      收件人邮箱
     * @param subject 邮件主题
     * @param content HTML内容
     */
    void sendHtmlEmail(String to, String subject, String content);
}