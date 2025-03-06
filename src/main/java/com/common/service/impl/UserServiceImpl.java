package com.common.service.impl;

import com.common.entity.Permission;
import com.common.entity.Role;
import com.common.entity.User;
import com.common.mapper.UserMapper;
import com.common.model.Result;
import com.common.model.UserInfo;
import com.common.model.dto.UserDTO;
import com.common.service.UserService;
import com.common.utils.CaptchaUtils;
import com.common.utils.JwtUtil;
import com.common.utils.PasswordUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

  @Autowired private UserMapper userMapper;

  @Autowired private HttpServletRequest request;

  @Autowired private JwtUtil jwtUtil;

  @Autowired private PasswordUtils passwordUtils;

  @Autowired private CaptchaUtils captchaUtils;

  private Long getCurrentUserId() {
    // 从请求中获取token
    String token = request.getHeader("Authorization");
    if (token != null && token.startsWith("Bearer ")) {
      token = token.substring(7);
      // 从token中解析用户ID
      try {
        return jwtUtil.getUserIdFromToken(token);
      } catch (Exception e) {
        return null;
      }
    }
    return null;
  }

  @Override
  public Result<User> login(String username, String password, String captcha) {
    String encryptedSessionCaptcha = (String) request.getSession().getAttribute("JSESSIONID");
    if (encryptedSessionCaptcha == null) {
      return Result.error(401, "验证码错误");
    }
    String sessionCaptcha = captchaUtils.decryptCaptcha(encryptedSessionCaptcha);
    if (!sessionCaptcha.equalsIgnoreCase(captcha)) {
      return Result.error(401, "验证码错误");
    }
    request.getSession().removeAttribute("JToken");
    User user = userMapper.findByUsername(username);
    if (user == null) {
      return Result.error(401, "用户名或密码错误");
    }
    if (!passwordUtils.matches(password, user.getPassword())) {
      return Result.error(401, "用户名或密码错误");
    }
    return Result.success(user);
  }

  @Override
  public Result<UserInfo> getUserInfo(Long userId) {
    UserInfo userInfo = getUserInfoInternal(userId);
    if (userInfo == null) {
      return Result.error(404, "用户不存在");
    }
    return Result.success(userInfo);
  }

  // 内部方法，用于获取用户信息
  private UserInfo getUserInfoInternal(Long userId) {
    User user = userMapper.findById(userId);
    if (user == null) {
      return null;
    }

    // 获取用户角色
    List<Role> roles = userMapper.findRolesByUserId(userId);

    // 获取用户权限
    List<Permission> permissions = new ArrayList<>();
    for (Role role : roles) {
      List<Permission> rolePermissions = userMapper.findPermissionsByRoleId(role.getId());
      permissions.addAll(rolePermissions);
    }

    // 构建用户信息
    UserInfo userInfo = new UserInfo();
    userInfo.setId(user.getId());
    userInfo.setUsername(user.getUsername());
    userInfo.setName(user.getName());
    userInfo.setGender(user.getGender());
    userInfo.setAge(user.getAge());
    userInfo.setAvatar(user.getAvatar());

    // 设置角色编码
    List<String> roleCodes = roles.stream().map(Role::getCode).collect(Collectors.toList());
    userInfo.setRoles(roleCodes);

    // 设置权限编码
    List<String> permissionCodes =
        permissions.stream().map(Permission::getCode).distinct().collect(Collectors.toList());
    userInfo.setPermissions(permissionCodes);

    return userInfo;
  }

  @Override
  public Result<List<User>> findAll() {
    // 获取当前用户的角色等级
    Long currentUserId = getCurrentUserId();
    UserInfo currentUser = getUserInfo(currentUserId).getData();
    int currentUserLevel = getHighestRoleLevel(currentUser.getRoles());

    List<User> allUsers = userMapper.findAll();
    List<User> filteredUsers =
        allUsers.stream()
            .filter(
                user -> {
                  List<Role> userRoles = userMapper.findRolesByUserId(user.getId());
                  int userLevel =
                      getHighestRoleLevel(
                          userRoles.stream().map(Role::getCode).collect(Collectors.toList()));
                  return userLevel >= currentUserLevel;
                })
            .collect(Collectors.toList());

    if (filteredUsers.isEmpty()) {
      return Result.error(403, "您没有权限查看用户列表");
    }

    return Result.success(filteredUsers);
  }

  @Override
  @Transactional
  public Result<Boolean> update(UserDTO userDTO) {
    Long currentUserId = getCurrentUserId();
    UserInfo currentUser = getUserInfo(currentUserId).getData();
    int currentUserLevel = getHighestRoleLevel(currentUser.getRoles());

    // 获取目标用户的角色等级
    List<Role> targetUserRoles = userMapper.findRolesByUserId(userDTO.getId());
    int targetUserLevel =
        getHighestRoleLevel(
            targetUserRoles.stream().map(Role::getCode).collect(Collectors.toList()));

    if (targetUserLevel <= currentUserLevel) {
      return Result.error(403, "您没有权限修改此用户信息");
    }

    // 执行更新操作
    User user = new User();
    BeanUtils.copyProperties(userDTO, user);
    int result = userMapper.update(user);

    if (userDTO.getRoleIds() != null) {
      userMapper.deleteUserRoles(user.getId());
      if (!userDTO.getRoleIds().isEmpty()) {
        userMapper.insertUserRoles(user.getId(), userDTO.getRoleIds());
      }
    }

    return Result.success(result > 0);
  }

  @Override
  @Transactional
  public Result<Boolean> delete(Long id) {
    Long currentUserId = getCurrentUserId();
    UserInfo currentUser = getUserInfo(currentUserId).getData();
    int currentUserLevel = getHighestRoleLevel(currentUser.getRoles());

    // 获取目标用户的角色等级
    List<Role> targetUserRoles = userMapper.findRolesByUserId(id);
    int targetUserLevel =
        getHighestRoleLevel(
            targetUserRoles.stream().map(Role::getCode).collect(Collectors.toList()));

    if (targetUserLevel <= currentUserLevel) {
      return Result.error(403, "您没有权限删除此用户");
    }

    // 执行删除操作
    userMapper.deleteUserRoles(id);
    int result = userMapper.delete(id);
    return Result.success(result > 0);
  }

  private int getHighestRoleLevel(List<String> roleCodes) {
    int highestLevel = Integer.MAX_VALUE;
    for (String roleCode : roleCodes) {
      int level = getRoleLevel(roleCode);
      if (level < highestLevel) {
        highestLevel = level;
      }
    }
    return highestLevel;
  }

  private int getRoleLevel(String roleCode) {
    switch (roleCode) {
      case "ROLE_SUPER_ADMIN":
        return 0;
      case "ROLE_ADMIN":
        return 1;
      case "ROLE_USER":
        return 2;
      default:
        return Integer.MAX_VALUE;
    }
  }

  @Override
  public Result<User> findById(Long id) {
    Long currentUserId = getCurrentUserId();
    UserInfo currentUser = getUserInfoInternal(currentUserId);
    int currentUserLevel = getHighestRoleLevel(currentUser.getRoles());

    User targetUser = userMapper.findById(id);
    if (targetUser == null) {
      return Result.error(404, "用户不存在");
    }

    // 获取目标用户的角色等级
    List<Role> targetUserRoles = userMapper.findRolesByUserId(id);
    int targetUserLevel =
        getHighestRoleLevel(
            targetUserRoles.stream().map(Role::getCode).collect(Collectors.toList()));

    if (targetUserLevel < currentUserLevel) {
      return Result.error(403, "您没有权限查看此用户信息");
    }

    return Result.success(targetUser);
  }

  @Override
  @Transactional
  public Result<Boolean> register(UserDTO userDTO) {
    // 检查用户名是否已存在
    User existingUser = userMapper.findByUsername(userDTO.getUsername());
    if (existingUser != null) {
      return Result.error(400, "用户名已存在");
    }

    // 创建新用户
    User user = new User();
    BeanUtils.copyProperties(userDTO, user);

    // 加密密码
    user.setPassword(passwordUtils.encryptForStorage(user.getPassword()));
    // 保存用户基本信息
    int result = userMapper.insert(user);

    // 分配普通用户角色
    Role userRole = userMapper.findRoleByCode("ROLE_USER");
    if (userRole != null) {
      List<Long> roleIds = new ArrayList<>();
      roleIds.add(userRole.getId());
      userMapper.insertUserRoles(user.getId(), roleIds);
    }

    return Result.success(result > 0);
  }
}
