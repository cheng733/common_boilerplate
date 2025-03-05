package com.common.controller;

import com.common.entity.User;
import com.common.model.LoginRequest;
import com.common.model.Result;
import com.common.model.UserInfo;
import com.common.service.UserService;
import com.common.utils.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "认证接口")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired private JwtUtil jwtUtil;

  @Autowired private UserService userService;

  @ApiOperation("登录")
  @PostMapping("/login")
  public Result<Map<String, Object>> login(
      @RequestBody LoginRequest loginRequest) { // 使用LoginRequest替代LoginDTO
    Result<User> userResult =
        userService.login(loginRequest.getUsername(), loginRequest.getPassword());
    if (!userResult.getCode().equals(200)) {
      return Result.error(userResult.getCode(), userResult.getMessage());
    }
    User user = userResult.getData();
    if (user != null) {
      // 生成token，传入userId
      String token = jwtUtil.generateToken(user.getUsername(), user.getId());

      // 获取用户权限信息
      Result<UserInfo> userInfoResult = userService.getUserInfo(user.getId());
      UserInfo userInfo = userInfoResult.getData();

      Map<String, Object> result = new HashMap<>();
      result.put("token", token);
      result.put("userInfo", userInfo);

      return Result.success(result);
    }
    return Result.error(401, "用户名或密码错误");
  }

  @GetMapping("/info")
  public Result<UserInfo> getUserInfo(@RequestAttribute Long currentUserId) {
    return userService.getUserInfo(currentUserId);
  }
}
