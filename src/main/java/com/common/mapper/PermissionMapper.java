package com.common.mapper;

import com.common.entity.Permission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PermissionMapper {
    
    /**
     * 查询所有API类型的权限
     */
    List<Permission> findAllApiPermissions();
}