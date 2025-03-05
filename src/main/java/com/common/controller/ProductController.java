package com.common.controller;

import com.common.entity.Product;
import com.common.model.Result;
import com.common.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "商品管理")
@RestController
@RequestMapping("/api/product")
public class ProductController {

  @Autowired private ProductService productService;

  @ApiOperation(value = "导入商品数据")
  @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Result<List<Product>> importExcel(@RequestPart("file") MultipartFile file) {
    try {
      List<Product> products = productService.importExcel(file);
      return Result.success(products);
    } catch (Exception e) {
      return Result.error("导入失败：" + e.getMessage());
    }
  }

  @ApiOperation(value = "下载导入模板", notes = "下载商品导入的Excel模板文件")
  @ApiResponses({
    @ApiResponse(code = 200, message = "下载成功"),
    @ApiResponse(code = 404, message = "模板文件不存在")
  })
  @GetMapping("/template")
  public ResponseEntity<byte[]> downloadTemplate() {
    try {
      byte[] template = productService.getTemplate();
      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=product_template.xlsx")
          .contentType(
              MediaType.parseMediaType(
                  "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
          .body(template);
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  @ApiOperation(value = "获取商品列表", notes = "获取所有商品数据")
  @ApiResponses({
    @ApiResponse(code = 200, message = "获取成功", response = Product.class, responseContainer = "List")
  })
  @GetMapping("/list")
  public Result<List<Product>> getList() {
    return Result.success(productService.getList());
  }
}
