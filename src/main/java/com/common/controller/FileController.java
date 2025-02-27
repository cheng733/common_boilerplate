package com.common.controller;

import com.common.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Api(tags = "文件上传接口")
@RestController
@RequestMapping("/api/file")
public class FileController {

    @Value("${file.upload-path}")
    private String uploadPath;

    @ApiOperation("上传图片")
    @PostMapping("/upload")
    public Result<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("文件为空");
        }

        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 生成新文件名
        String newFileName = UUID.randomUUID().toString() + suffix;
        
        // 创建文件存储目录
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 创建新文件
        File dest = new File(uploadPath + File.separator + newFileName);
        try {
            file.transferTo(dest);
            return Result.success("/uploads/" + newFileName);
        } catch (IOException e) {
            return Result.error("文件上传失败");
        }
    }
}