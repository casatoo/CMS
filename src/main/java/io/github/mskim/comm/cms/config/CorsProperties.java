package io.github.mskim.comm.cms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component @Data
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {
    private List<String> allowed;
}
