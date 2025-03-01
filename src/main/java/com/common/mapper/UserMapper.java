package com.common.mapper;

import com.common.entity.Permission;
import com.common.entity.Role;
import com.common.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    
    User findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
    
    User findById(@Param("id") Long id);
    
    Role findRoleById(Long roleId);
    
    List<Role> findRolesByUserId(@Param("userId") Long userId);
    
    List<Permission> findPermissionsByRoleId(@Param("roleId") Long roleId);
    
    List<User> findAll();
    
    int insert(User user);
    
    int update(User user);
    
    int delete(@Param("id") Long id);
    
    void insertUserRoles(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);
    
    void deleteUserRoles(@Param("userId") Long userId);
}