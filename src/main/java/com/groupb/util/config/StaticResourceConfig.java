package com.groupb.util.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 静态资源配置
 * 配置上传文件的访问路径
 */
@Slf4j
@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Value("${file.upload.path:F:/image}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // 配置上传文件的访问路径
        // 访问 /uploads/** 路径时，实际访问 F:/image/** 目录下的文件
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath + "/");
        
        log.info("配置静态资源访问路径: /uploads/** -> file:{}/", uploadPath);
    }
}
