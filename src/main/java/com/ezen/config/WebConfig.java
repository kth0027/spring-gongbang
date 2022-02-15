package com.ezen.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 1. Tomcat 에서 local file 에 접근 권한이 없기 때문에
// 2. 파일에 접근하려면 WebConfig 를 추가로 작성해야합니다.
// 3.

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${resource.path}")
    private String resourcePath;

    @Value("${upload.path}")
    private String uploadPath;

//    @Override
//    public void addResourceHandler(ResourceHandlerRegistry registry){
//        registry.addResourceHandler(uploadPath)
//                .addResourceLocations(resourcePath);
//    }
}
