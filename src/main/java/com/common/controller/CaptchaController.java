package com.common.controller;

import com.common.utils.CaptchaUtils;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/captcha")
public class CaptchaController {

  @Autowired private DefaultKaptcha defaultKaptcha;

  @Autowired private CaptchaUtils captchaUtils;

  @GetMapping("/generate")
  public void generateCaptcha(HttpSession session, HttpServletResponse response) throws Exception {
    String code = defaultKaptcha.createText();
    // 加密验证码后存储
    String encryptedCode = captchaUtils.encryptCaptcha(code);
    session.setAttribute("JSESSIONID", encryptedCode);

    // 设置响应类型和session相关头信息
    response.setContentType("image/jpeg");
    response.setHeader("Cache-Control", "no-store, no-cache");
    response.setHeader(
        "Set-Cookie", "JToken=" + session.getId() + "; Path=/; HttpOnly; SameSite=Strict");
    response.setHeader("Access-Control-Allow-Credentials", "true");

    // 生成验证码图片并输出
    BufferedImage image = defaultKaptcha.createImage(code);
    ServletOutputStream out = response.getOutputStream();
    ImageIO.write(image, "jpg", out);
    out.flush();
  }
}
