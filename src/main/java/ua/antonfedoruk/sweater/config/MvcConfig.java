package ua.antonfedoruk.sweater.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Value("${upload.path}")
    private String uploadPath;

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }

    //we need this to get files from dir
    //We override the addResourceHandlers() to register handlers and locations for img files
    @Override //when we try ty connect to server with path "/img/***" --> server will redirect us to \ location
    public void addResourceHandlers(ResourceHandlerRegistry registry) {                          //  |
        registry                                                                                 //  |
                .addResourceHandler("/img/**")                                      //  V
                .addResourceLocations("file://" + uploadPath + "/");  //  "file://" - store in filesystem + "uploadPath" - absolute path
        registry.addResourceHandler("/static/**").
                addResourceLocations("classpath:/static/"); // "classpath:"- resources will be looking in the tree-project
    }
}