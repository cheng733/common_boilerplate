package com.common.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Map;

@Slf4j
@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private TemplateEngine templateEngine;
    
    @Value("${spring.mail.username}")
    private String from;
    
    public void sendTemplateEmail(String to, String subject, String template, Map<String, Object> variables) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            // 设置发件人和收件人
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            
            // 处理模板
            Context context = new Context();
            context.setVariables(variables);
            String content = templateEngine.process(template, context);
            
            helper.setText(content, true);
            mailSender.send(message);
            
            log.info("模板邮件已发送至：{}", to);
        } catch (Exception e) {
            log.error("发送邮件失败：", e);
            throw new RuntimeException("发送邮件失败：" + e.getMessage());
        }
    }
}