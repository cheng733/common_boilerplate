package com.common.model;

import lombok.Data;
import java.util.List;

@Data
public class UserInfo {
    private Long id;
    private String username;
    private String name;
    private Integer gender;
    private Integer age;
    private String avatar;
    private List<String> roles;
    private List<String> permissions;
}