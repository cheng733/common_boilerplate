package com.common.service;

import com.common.entity.User;
import com.common.model.Result;
import com.common.model.UserInfo;
import com.common.model.dto.UserDTO;
import java.util.List;

public interface UserService {

  /** 用户登录 */
  Result<User> login(String username, String password);

  /** 获取用户信息（包含角色和权限） */
  Result<UserInfo> getUserInfo(Long userId);

  /** 获取所有用户 */
  Result<List<User>> findAll();

  /** 根据ID获取用户 */
  Result<User> findById(Long id);

  /** 更新用户 */
  Result<Boolean> update(UserDTO userDTO);

  /** 删除用户 */
  Result<Boolean> delete(Long id);

  /** 用户注册 */
  Result<Boolean> register(UserDTO userDTO);
}
