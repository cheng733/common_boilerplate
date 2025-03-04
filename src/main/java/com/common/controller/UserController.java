package com.common.controller;

import com.common.entity.User;
import com.common.model.Result;
import com.common.model.dto.UserDTO;
import com.common.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "用户管理")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("获取用户列表")
    @GetMapping("/list")
    public List<User> list() {
        return userService.findAll().getData();
    }
    
    @ApiOperation("根据ID获取用户")
    @GetMapping("/{id}")
    public User getById(@ApiParam(value = "用户ID", required = true) @PathVariable Long id) {
        return userService.findById(id).getData();
    }

    @ApiOperation("新增用户")
    @PostMapping("/register")
    public Result<Boolean> register(@ApiParam(value = "用户信息", required = true) @RequestBody UserDTO userDTO) {
        return userService.register(userDTO);
    }

    @ApiOperation("更新用户")
    @PutMapping
    public Boolean update(@ApiParam(value = "用户信息", required = true) @RequestBody UserDTO userDTO) {
        return userService.update(userDTO).getData();
    }

    @ApiOperation("删除用户")
    @DeleteMapping("/{id}")
    public Boolean delete(@ApiParam(value = "用户ID", required = true) @PathVariable Long id) {
        return userService.delete(id).getData();
    }
}