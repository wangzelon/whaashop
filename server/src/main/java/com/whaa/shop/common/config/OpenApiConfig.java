package com.whaa.shop.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    private static final String JWT_SCHEME = "BearerAuth";

    @Bean
    OpenAPI whaaShopOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("WhaaShop 商城后端接口文档")
                        .description("商城前台、运营后台及智能客服 OpenAPI 文档")
                        .version("v1")
                        .contact(new Contact().name("WhaaShop 技术团队")))
                .components(new Components().addSecuritySchemes(JWT_SCHEME,
                        new SecurityScheme()
                                .name(JWT_SCHEME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(JWT_SCHEME));
    }
}
