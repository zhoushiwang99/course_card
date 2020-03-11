package cn.edu.csust.coursecard.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zsw
 * @date 2019/11/18 22:42
 */
@Data
@Component
@ConfigurationProperties(prefix = "my.config")
public class UriConfig {

    private List<String> includeUri;
    private List<String> excludeUri;

}
