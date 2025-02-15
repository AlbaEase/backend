package com.example.albaease.common.config;

<<<<<<< HEAD
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
=======
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
>>>>>>> c3e0c67f241020bc3b79dcbc0ff6ead3bd80bcd7
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
<<<<<<< HEAD

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("알바이즈 API")
                        .description("알바이즈 프로젝트 API 명세서")
                        .version("v1.0.0"));
    }
}
=======
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearer-key", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"))
                )
                .addSecurityItem(new SecurityRequirement().addList("bearer-key"));
    }
}

>>>>>>> c3e0c67f241020bc3b79dcbc0ff6ead3bd80bcd7
