package com.common.controller;

import com.common.entity.CommonEntity;
import com.common.service.CommonService;
import com.common.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "通用接口")
@RestController
@RequestMapping("/api/common")
public class CommonController {
    
    @Autowired
    private CommonService commonService;
    
    @ApiOperation("根据ID查询")
    @GetMapping("/{id}")
    public Result<CommonEntity> getById(@ApiParam(value = "主键ID", required = true) @PathVariable Long id) {
        return Result.success(commonService.getById(id));
    }
    
    @ApiOperation("查询列表")
    @GetMapping("/list")
    public Result<List<CommonEntity>> list() {
        return Result.success(commonService.list());
    }
    
    @ApiOperation("新增")
    @PostMapping
    public Result<Boolean> save(@ApiParam(value = "实体对象", required = true) @RequestBody CommonEntity entity) {
        return Result.success(commonService.save(entity));
    }
    
    @ApiOperation("更新")
    @PutMapping
    public Result<Boolean> update(@ApiParam(value = "实体对象", required = true) @RequestBody CommonEntity entity) {
        return Result.success(commonService.update(entity));
    }
    
    @ApiOperation("删除")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@ApiParam(value = "主键ID", required = true) @PathVariable Long id) {
        return Result.success(commonService.delete(id));
    }
}