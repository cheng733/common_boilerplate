package com.common.model;

import java.util.List;
import lombok.Data;

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
