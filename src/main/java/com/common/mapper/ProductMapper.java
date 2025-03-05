package com.common.mapper;

import com.common.entity.Product;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {
  int insert(Product product);

  List<Product> selectList();
}
