package com.common.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.Data;

@Data
@ApiModel(description = "商品信息")
public class Product {
  @ApiModelProperty(value = "商品ID", example = "1")
  private Long id;

  @ApiModelProperty(value = "商品名称", required = true, example = "测试商品")
  private String name;

  @ApiModelProperty(value = "库存数量", required = true, example = "100")
  private Integer stock;

  @ApiModelProperty(value = "商品编号", required = true, example = "P001")
  private String code;

  @ApiModelProperty(value = "商品价格", required = true, example = "99.99")
  private BigDecimal price;
}
