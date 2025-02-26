package com.common.controller;

import com.common.model.LoginRequest;
import com.common.model.Result;
import com.common.utils.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "认证接口")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @ApiOperation("登录")
    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginRequest loginRequest) {
        if ("admin".equals(loginRequest.getUsername()) && 
            "123456".equals(loginRequest.getPassword())) {
            String token = jwtUtil.generateToken(loginRequest.getUsername());
            return Result.success(token);
        }
        return Result.error("用户名或密码错误");
    }
}