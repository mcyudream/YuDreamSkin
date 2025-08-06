package online.yudream.yudreamskin.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * online.yudream.platform.config
 *
 * @author SiberianHusky
 * * @date 2025/7/5
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "security.excluded-urls")
public class SecurityExcludedUrlsConfig {

    private List<String> urls;

}