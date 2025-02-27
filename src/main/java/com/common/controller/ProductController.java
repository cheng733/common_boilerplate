package com.common.controller;

import com.common.entity.Product;
import com.common.model.Result;
import com.common.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = "商品接口")
@RestController
@RequestMapping("/api/product")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @ApiOperation("导入Excel")
    @PostMapping("/import")
    public Result<List<Product>> importExcel(@RequestParam("file") MultipartFile file) {
        try {
            List<Product> products = productService.importExcel(file);
            return Result.success(products);
        } catch (Exception e) {
            return Result.error("导入失败：" + e.getMessage());
        }
    }
    
    @ApiOperation("下载模板")
    @GetMapping("/template")
    public ResponseEntity<byte[]> downloadTemplate() {
        try {
            byte[] template = productService.getTemplate();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=product_template.xlsx")
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(template);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @ApiOperation("获取商品列表")
    @GetMapping("/list")
    public Result<List<Product>> getList() {
        return Result.success(productService.getList());
    }
}