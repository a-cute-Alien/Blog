package com.wzc.blog.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Profile("dev")
public class SwaggerConfig {
    @Bean
    public Docket getDocket1(){
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(true)
                .groupName("wzc")
                .apiInfo(apiInfo())
                .select().paths(PathSelectors.regex("(?!/admin).*"))
                .build();
    }
    @Bean
    public Docket getDocket2(){
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(true)
                .groupName("admin")
                .apiInfo(apiInfo())
                .select().apis(RequestHandlerSelectors.basePackage("com.wzc.blog.controller.admin"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Blog Interface")
                .description("博客系统后端接口")
                .termsOfServiceUrl("http://localhost:8091")
                .contact(contact())
                .version("1.0")
                .build();
    }
    private Contact contact(){
        return new Contact("wzc","http://localhost:8091/about","954845887@qq.com");
    }
}
