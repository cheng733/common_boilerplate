package com.common.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "通用实体类")
public class CommonEntity {
  @ApiModelProperty(value = "主键ID", example = "1")
  private Long id;

  @ApiModelProperty(value = "名称", example = "测试名称")
  private String name;

  @ApiModelProperty(value = "描述", example = "测试描述")
  private String description;
}
