package com.common.mapper;

import com.common.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ProductMapper {
    int insert(Product product);
    List<Product> selectList();
}