package com.common.service;

import com.common.entity.Product;
import com.common.mapper.ProductMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductService {

  @Autowired private ProductMapper productMapper;

  public List<Product> importExcel(MultipartFile file) throws IOException {
    List<Product> products = new ArrayList<>();

    try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
      Sheet sheet = workbook.getSheetAt(0);

      for (int i = 1; i <= sheet.getLastRowNum(); i++) {
        Row row = sheet.getRow(i);
        if (row == null) continue;

        Product product = new Product();
        product.setName(getCellValue(row.getCell(0)));
        product.setStock(Integer.parseInt(getCellValue(row.getCell(1))));
        product.setCode(getCellValue(row.getCell(2)));
        product.setPrice(new BigDecimal(getCellValue(row.getCell(3))));

        productMapper.insert(product);
        products.add(product);
      }
    }

    return products;
  }

  private String getCellValue(Cell cell) {
    if (cell == null) return "";

    switch (cell.getCellType()) {
      case STRING:
        return cell.getStringCellValue();
      case NUMERIC:
        return String.valueOf((int) cell.getNumericCellValue());
      default:
        return "";
    }
  }

  public byte[] getTemplate() throws IOException {
    ClassPathResource resource = new ClassPathResource("templates/product_template.xlsx");
    try (InputStream is = resource.getInputStream();
        Workbook workbook = new XSSFWorkbook(is);
        ByteArrayOutputStream os = new ByteArrayOutputStream()) {
      workbook.write(os);
      return os.toByteArray();
    }
  }

  public List<Product> getList() {
    return productMapper.selectList();
  }
}
