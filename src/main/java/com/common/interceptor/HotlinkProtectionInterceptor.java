package com.common.interceptor;

import com.common.config.HotlinkProtectionConfig;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class HotlinkProtectionInterceptor implements HandlerInterceptor {

  @Autowired private HotlinkProtectionConfig config;

  private final AntPathMatcher pathMatcher = new AntPathMatcher();

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    // 如果未启用防盗链，直接放行
    if (!config.isEnabled()) {
      return true;
    }
    // 检查请求路径是否需要防盗链保护
    String requestPath = request.getRequestURI();
    boolean needsProtection = false;
    for (String protectedPath : config.getResourcePaths()) {
      if (pathMatcher.match(protectedPath, requestPath)) {
        needsProtection = true;
        break;
      }
    }
    if (!needsProtection) {
      return true;
    }
    // 获取请求的Referer
    String referer = request.getHeader("Referer");
    System.out.println(referer == null);
    if (referer == null) {
      // 没有Referer，拒绝访问
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      return false;
    }

    // 检查Referer是否在允许的域名列表中
    try {
      URL refererUrl = new URL(referer);
      String refererHost = refererUrl.getHost();

      // 检查域名
      for (String allowedDomain : config.getAllowedDomains()) {
        if (refererHost.endsWith(allowedDomain)) {
          return true;
        }
      }

      // 检查IP
      for (String allowedIp : config.getAllowedIps()) {
        if (refererHost.equals(allowedIp)) {
          return true;
        }
      }

      // 获取客户端IP并检查
      String clientIp = getClientIp(request);
      if (config.getAllowedIps().contains(clientIp)) {
        return true;
      }

      // 不在允许列表中，拒绝访问
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      return false;

    } catch (MalformedURLException e) {
      // URL格式错误，拒绝访问
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      return false;
    }
  }

  private String getClientIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");
    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    // 多个代理的情况，第一个IP为客户端真实IP
    if (ip != null && ip.contains(",")) {
      ip = ip.split(",")[0].trim();
    }
    return ip;
  }
}
