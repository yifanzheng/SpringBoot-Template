package top.yifan.config.apidoc;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {
    
    private String scanPackage;
    
    private boolean actuatorEnable;
    
    private Map<String, String> tags;

    public String getScanPackage() {
        return scanPackage;
    }

    public void setScanPackage(String scanPackage) {
        this.scanPackage = scanPackage;
    }

    public boolean isActuatorEnable() {
        return actuatorEnable;
    }

    public void setActuatorEnable(boolean actuatorEnable) {
        this.actuatorEnable = actuatorEnable;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }
    
}
