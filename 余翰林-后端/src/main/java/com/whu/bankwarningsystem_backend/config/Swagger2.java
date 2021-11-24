package com.whu.bankwarningsystem_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2 {
    /**
     * @Description:设置Swagger2扫描的基包
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.whu.bankwarningsystem_backend"))
                .apis(RequestHandlerSelectors.withClassAnnotation(Controller.class))
                .paths(PathSelectors.any())
                .build();
    }
    /**
     * @Description: 构建 api文档的信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 设置页面标题
                .title("Market中心后端api接口文档")
                // 描述
                .description("欢迎访问Market中心接口文档，这里是描述信息")
                // 定义版本号
                .version("1.0").build();
    }
}
