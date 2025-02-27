package com.common.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "邮件发送请求")
public class EmailDTO {
    @ApiModelProperty(value = "收件人", required = true, example = "example@qq.com")
    private String to;
    
    @ApiModelProperty(value = "标题", required = true, example = "测试邮件")
    private String title;
    
    @ApiModelProperty(value = "用户名", required = true, example = "张三")
    private String userName;
    
    @ApiModelProperty(value = "内容", required = true, example = "这是一封测试邮件")
    private String content;
}