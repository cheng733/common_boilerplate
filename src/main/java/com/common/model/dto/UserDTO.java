package com.common.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

@Data
@ApiModel(description = "用户数据传输对象")
public class UserDTO {

  @ApiModelProperty(value = "用户ID")
  private Long id;

  @ApiModelProperty(value = "用户名", required = true)
  private String username;

  @ApiModelProperty(value = "密码")
  private String password;

  @ApiModelProperty(value = "姓名", required = true)
  private String name;

  @ApiModelProperty(value = "性别：0-女，1-男", required = true)
  private Integer gender;

  @ApiModelProperty(value = "年龄")
  private Integer age;

  @ApiModelProperty(value = "头像地址")
  private String avatar;

  @ApiModelProperty(value = "角色ID列表")
  private List<Long> roleIds;
}
