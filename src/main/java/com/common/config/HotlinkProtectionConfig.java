package com.common.config;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "hotlink.protection")
@Data
public class HotlinkProtectionConfig {
  private boolean enabled;
  private List<String> allowedDomains;
  private List<String> allowedIps;
  private List<String> resourcePaths;
}
