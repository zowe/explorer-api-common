package org.zowe.api.common.connectors.zosmf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("connection")
public class ConnectionProperties {
    
    private String ipAddress;
    private Integer httpsPort;
}
