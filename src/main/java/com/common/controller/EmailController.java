package com.common.controller;

import com.common.model.Result;
import com.common.model.dto.EmailDTO; // 添加这行
import com.common.service.EmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "邮件服务")
@RestController
@RequestMapping("/api/email")
public class EmailController {

  @Autowired private EmailService emailService;

  @ApiOperation("发送模板邮件")
  @PostMapping("/template")
  public Result<Void> sendTemplateEmail(@RequestBody EmailDTO emailDTO) {
    try {
      Map<String, Object> variables = new HashMap<>();
      variables.put("title", emailDTO.getTitle());
      variables.put("userName", emailDTO.getUserName());
      variables.put("content", emailDTO.getContent());
      variables.put("sendTime", new Date());

      emailService.sendTemplateEmail(
          emailDTO.getTo(), emailDTO.getTitle(), "mail/notification", variables);
      return Result.success(null);
    } catch (Exception e) {
      return Result.error("邮件发送失败：" + e.getMessage());
    }
  }
}
