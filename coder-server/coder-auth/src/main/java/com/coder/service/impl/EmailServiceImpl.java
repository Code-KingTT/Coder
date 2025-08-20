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

    @Value("${spring.mail.username}")
    private String smtpUsername;

    @Value("${mail.from.address:life@coder.com}")
    private String displayAddress;

    @Value("${mail.from.name:Coder}")
    private String fromName;

    @Override
    public void sendSimpleEmail(String to, String subject, String content) {
        log.info("发送简单邮件，收件人：{}，主题：{}", to, subject);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            // 发件人使用真实邮箱
            helper.setFrom(String.format("%s <%s>", fromName, smtpUsername));
            // 回复地址使用虚拟邮箱
            helper.setReplyTo(String.format("%s <%s>", fromName, displayAddress));

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, false);

            mailSender.send(message);
            log.info("简单邮件发送成功，收件人：{}", to);

        } catch (Exception e) {
            log.error("发送简单邮件失败，收件人：{}，错误：{}", to, e.getMessage(), e);
            throw new RuntimeException("邮件发送失败", e);
        }
    }

    @Override
    public void sendHtmlEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // 发件人使用真实邮箱
            helper.setFrom(String.format("%s <%s>", fromName, smtpUsername));
            // 回复地址使用虚拟邮箱
            helper.setReplyTo(String.format("%s <%s>", fromName, displayAddress));

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
            log.info("HTML邮件发送成功，收件人：{}", to);

        } catch (Exception e) {
            log.error("发送HTML邮件失败：{}", e.getMessage(), e);
            throw new RuntimeException("邮件发送失败", e);
        }
    }
}