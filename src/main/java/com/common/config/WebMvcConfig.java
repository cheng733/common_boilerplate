package com.common.config;

import com.common.interceptor.PermissionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Autowired private PermissionInterceptor permissionInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // 添加日志
    System.out.println("注册权限拦截器");

    // 注册权限拦截器，拦截所有API请求
    registry
        .addInterceptor(permissionInterceptor)
        .addPathPatterns("/api/**")
        .excludePathPatterns("/api/auth/**") // 排除登录接口
        .excludePathPatterns("/api/user/register");
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // 添加静态资源处理器，用于访问上传的图片

    registry.addResourceHandler("/uploads/**").addResourceLocations("file:d:/common_boil/uploads/");

    // 如果您使用的是相对路径，也可以这样配置
    // registry.addResourceHandler("/uploads/**")
    //         .addResourceLocations("file:./uploads/");
  }
}
