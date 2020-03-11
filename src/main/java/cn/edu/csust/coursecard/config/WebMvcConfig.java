package cn.edu.csust.coursecard.config;

import cn.edu.csust.coursecard.bean.UriConfig;
import cn.edu.csust.coursecard.interceptor.AuthorizationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zsw
 * @date 2019/11/18 22:41
 */
@Component
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private UriConfig uriConfig;

    @Autowired
    AuthorizationInterceptor authorizationInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 设置允许跨域的路径
        registry.addMapping("/**")
                // 设置允许跨域请求的域名
                .allowedOrigins("*")
                // 是否允许证书 不再默认开启
                .allowCredentials(true)
                // 设置允许的方法
                .allowedMethods("*")
                // 跨域允许时间
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor).addPathPatterns(uriConfig.getIncludeUri()).excludePathPatterns(uriConfig.getExcludeUri());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/apk/**").addResourceLocations("file:/home/zyh/apk/");
//        registry.addResourceHandler("/apk/**").addResourceLocations("file:C:\\Users\\zsw\\Desktop\\");
    }
}
