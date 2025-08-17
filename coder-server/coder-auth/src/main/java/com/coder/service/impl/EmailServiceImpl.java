package com.coder.service.impl;

import com.coder.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.Date;

/**
 * 邮件服务实现类
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Resource
    private JavaMailSender mailSender;

    @Value("${mail.from.address}")
    private String fromAddress;

    @Value("${mail.from.name}")
    private String fromName;

    @Override
    public void sendSimpleEmail(String to, String subject, String content) {
        log.info("发送简单邮件，收件人：{}，主题：{}", to, subject);
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            String from = String.format("%s <%s>", fromName, fromAddress);
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            message.setSentDate(new Date());
            
            mailSender.send(message);
            log.info("简单邮件发送成功，收件人：{}", to);
            
        } catch (Exception e) {
            log.error("发送简单邮件失败，收件人：{}，错误：{}", to, e.getMessage(), e);
            throw new RuntimeException("邮件发送失败", e);
        }
    }

    @Override
    public void sendHtmlEmail(String to, String subject, String content) {
        log.info("发送HTML邮件，收件人：{}，主题：{}", to, subject);
        
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            String from = String.format("%s <%s>", fromName, fromAddress);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            
            mailSender.send(message);
            log.info("HTML邮件发送成功，收件人：{}", to);
            
        } catch (Exception e) {
            log.error("发送HTML邮件失败，收件人：{}，错误：{}", to, e.getMessage(), e);
            throw new RuntimeException("邮件发送失败", e);
        }
    }
}