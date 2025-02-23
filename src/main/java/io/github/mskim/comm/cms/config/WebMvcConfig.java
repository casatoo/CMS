package io.github.mskim.comm.cms.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // viewController
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("loginView");
        registry.addViewController("/join").setViewName("joinView");
        registry.addViewController("/main").setViewName("domain/mainView");
    }

    // 정적 리소스 핸들링 (CSS, JS, 이미지 서빙)
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/img/**").addResourceLocations("classpath:/static/img/");
    }

    // 코드값을 중앙에서 관리하고, 하드코딩을 피할 수 있도록
    @Getter
    @RequiredArgsConstructor
    public enum Category {
        MAIN("MAIN"),
        USER("USER"),
        ADMIN("ADMIN");

        private final String code;
    }

}
