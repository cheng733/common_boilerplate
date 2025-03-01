package com.common.interceptor;

import com.common.entity.Permission;
import com.common.mapper.PermissionMapper;
import com.common.model.UserInfo;
import com.common.model.Result;
import com.common.service.UserService;
import com.common.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PermissionInterceptor implements HandlerInterceptor {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private ObjectMapper objectMapper;
    
    // 权限编码到URL的映射缓存
    private Map<String, String> permissionUrlMap = new HashMap<>();
    
    @PostConstruct
    public void init() {
        // 系统启动时加载所有权限URL映射
        loadPermissionUrls();
    }
    
    /**
     * 加载所有权限URL映射
     */
    private void loadPermissionUrls() {
        List<Permission> permissions = permissionMapper.findAllApiPermissions();
        for (Permission permission : permissions) {
            if (permission.getUrl() != null && permission.getCode() != null) {
                permissionUrlMap.put(permission.getCode(), permission.getUrl());
            }
        }
    }
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 添加日志
        System.out.println("拦截器执行: " + request.getRequestURI());
        
        // 获取请求路径
        String requestURI = request.getRequestURI();
        
        // 从请求头获取token
        String token = request.getHeader("Authorization");
        System.out.println("Authorization头: " + token);
        
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        // 验证token
        if (token == null || !jwtUtil.validateToken(token)) {
            // token不存在或无效，返回401未授权
            System.out.println("token验证失败，返回401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            Result<String> result = Result.error(401, "请先登录");
            response.getWriter().write(objectMapper.writeValueAsString(result));
            return false;
        }
        
        // 获取用户信息
        Long userId = jwtUtil.getUserIdFromToken(token);
        Result<UserInfo> userInfoResult = userService.getUserInfo(userId);
        UserInfo userInfo = userInfoResult.getData();
        
        // 检查用户是否有权限访问该路径
        if (userInfo != null && hasPermission(userInfo, requestURI)) {
            return true;
        }
        
        // 已登录但无权限，返回403
        System.out.println("权限验证失败，返回403");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        Result<String> result = Result.error(403, "您没有访问权限");
        response.getWriter().write(objectMapper.writeValueAsString(result));
        return false;
    }
    
    /**
     * 检查用户是否有权限
     */
    private boolean hasPermission(UserInfo userInfo, String requestURI) {
        // 添加日志
        System.out.println("检查权限: " + requestURI);
        System.out.println("用户角色: " + userInfo.getRoles());
        System.out.println("用户权限: " + userInfo.getPermissions());
        System.out.println("权限URL映射: " + permissionUrlMap);
        
        // 超级管理员拥有所有权限
        if (userInfo.getRoles().contains("ROLE_SUPER_ADMIN")) {
            System.out.println("超级管理员权限通过");
            return true;
        }
        
        // 检查用户权限列表是否包含该路径
        for (String permission : userInfo.getPermissions()) {
            String permissionUrl = permissionUrlMap.get(permission);
            System.out.println("当前检查权限编码: " + permission);
            System.out.println("对应的URL: " + permissionUrl);
            
            if (permissionUrl != null) {
                // 去除首尾的空格
                permissionUrl = permissionUrl.trim();
                String normalizedRequestURI = requestURI.trim();
                      
                // 支持路径参数匹配，如 /api/user/{id} 可以匹配 /api/user/1
                boolean match = matchUrl(permissionUrl, normalizedRequestURI);
                System.out.println("URL匹配结果: " + match);
                
                if (match) {
                    System.out.println("找到匹配的权限，验证通过");
                    return true;
                }
            }
        }
        
        System.out.println("未找到匹配的权限，验证失败");
        return false;
    }
    
    /**
     * 匹配URL，支持路径参数
     */
    private boolean matchUrl(String permissionUrl, String requestUrl) {
        // 完全相等
        if (permissionUrl.equals(requestUrl)) {
            System.out.println("完全匹配成功");
            return true;
        }
        
        // 处理路径参数，如 /api/user/{id} 可以匹配 /api/user/1
        if (permissionUrl.contains("{") && permissionUrl.contains("}")) {
            String[] permissionParts = permissionUrl.split("/");
            String[] requestParts = requestUrl.split("/");
            
            if (permissionParts.length != requestParts.length) {
                System.out.println("路径长度不匹配");
                return false;
            }
            
            for (int i = 0; i < permissionParts.length; i++) {
                String permPart = permissionParts[i];
                String reqPart = requestParts[i];
                
                // 如果是路径参数，跳过比较
                if (permPart.startsWith("{") && permPart.endsWith("}")) {
                    continue;
                }
                
                // 其他部分必须完全匹配
                if (!permPart.equals(reqPart)) {
                    System.out.println("路径部分不匹配: " + permPart + " != " + reqPart);
                    return false;
                }
            }
            
            System.out.println("路径参数匹配成功");
            return true;
        }
        
        return false;
    }
}