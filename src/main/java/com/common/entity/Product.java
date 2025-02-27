package com.common.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Product {
    private Long id;
    private String name;
    private Integer stock;
    private String code;
    private BigDecimal price;
}