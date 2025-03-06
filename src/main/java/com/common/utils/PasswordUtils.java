package com.common.utils;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtils {

  private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  private static final String KEY = "CommonBoilSecKey"; // 16位密钥
  private static final String ALGORITHM = "AES";

  public String encryptForStorage(String transferredPassword) {
    // 先解密传输密码
    String decodedPassword = new String(Base64.getDecoder().decode(transferredPassword));
    // 再用BCrypt加密存储
    return encoder.encode(decodedPassword);
  }

  public String decryptFromStorage(String encryptedPassword) {
    try {
      SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, keySpec);
      byte[] decoded = Base64.getDecoder().decode(encryptedPassword);
      byte[] decrypted = cipher.doFinal(decoded);
      return new String(decrypted);
    } catch (Exception e) {
      throw new RuntimeException("解密失败", e);
    }
  }

  // 密码验证
  public boolean matches(String transferredPassword, String storedPassword) {
    // 先解密传输密码
    String decodedPassword = new String(Base64.getDecoder().decode(transferredPassword));
    // 验证密码
    return encoder.matches(decodedPassword, storedPassword);
  }
}
