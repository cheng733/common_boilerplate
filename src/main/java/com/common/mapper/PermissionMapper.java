package com.common.mapper;

import com.common.entity.Permission;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PermissionMapper {

  /** 查询所有API类型的权限 */
  List<Permission> findAllApiPermissions();
}
