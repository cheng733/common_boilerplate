package com.common.utils;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Properties;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CaptchaUtils {

  private static final String KEY = "CaptchaSecretKey"; // 16位密钥
  private static final String ALGORITHM = "AES";

  @Bean
  public DefaultKaptcha getDefaultKaptcha() {
    DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
    Properties properties = new Properties();
    properties.setProperty("kaptcha.border", "yes");
    properties.setProperty("kaptcha.border.color", "105,179,90");
    properties.setProperty("kaptcha.textproducer.font.color", "blue");
    properties.setProperty("kaptcha.image.width", "110");
    properties.setProperty("kaptcha.image.height", "40");
    properties.setProperty("kaptcha.textproducer.font.size", "30");
    properties.setProperty("kaptcha.session.key", "code");
    properties.setProperty("kaptcha.textproducer.char.length", "4");
    properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
    Config config = new Config(properties);
    defaultKaptcha.setConfig(config);
    return defaultKaptcha;
  }

  public String generateCaptchaImage(DefaultKaptcha defaultKaptcha, String code) throws Exception {
    BufferedImage image = defaultKaptcha.createImage(code);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ImageIO.write(image, "jpg", outputStream);
    return Base64.getEncoder().encodeToString(outputStream.toByteArray());
  }

  public String encryptCaptcha(String captcha) {
    try {
      SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, keySpec);
      byte[] encrypted = cipher.doFinal(captcha.getBytes());
      return Base64.getEncoder().encodeToString(encrypted);
    } catch (Exception e) {
      throw new RuntimeException("验证码加密失败", e);
    }
  }

  public String decryptCaptcha(String encryptedCaptcha) {
    try {
      SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, keySpec);
      byte[] decoded = Base64.getDecoder().decode(encryptedCaptcha);
      byte[] decrypted = cipher.doFinal(decoded);
      return new String(decrypted);
    } catch (Exception e) {
      throw new RuntimeException("验证码解密失败", e);
    }
  }
}
