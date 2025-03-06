package com.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "登录请求参数")
public class LoginRequest {
  @ApiModelProperty(value = "用户名", required = true, example = "admin")
  private String username;

  @ApiModelProperty(value = "密码", required = true, example = "123456")
  private String password;

  @ApiModelProperty(value = "验证码", required = true, example = "m5xn")
  private String captcha;
}
