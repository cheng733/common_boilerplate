package com.common.entity;

import lombok.Data;
import java.util.Date;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String name;
    private Integer gender;
    private Integer age;
    private String avatar;
    private Date createTime;
    private Date updateTime;
}