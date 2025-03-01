package com.common.entity;

import lombok.Data;

@Data
public class Permission {
    private Long id;
    private String name;
    private String code;
    private String type;
    private String url;
    private Long parentId;
    private Integer sort;
}