package com.alfred.backoffice.modules.auth.infrastructure.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    @Bean
    public OpenAPI openAPI() {
        // TODO: Set amg-community header as required
        return new OpenAPI().addSecurityItem(new SecurityRequirement().
                        addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme())
                        .addHeaders("amg-community", new Header()))
                .info(new Info().title("Alfred MG API")
                        .description("API of Alfred MG")
                        .version("1.0").contact(new Contact().name("Alfred MG Team")
                                .email("alfredmgconfig@gmail.com").url("https://github.com/Alfred-Manager")));
    }

    protected boolean shouldNotAddHeader(String path) {
        // TODO: Refactor this method due to duplicity in FirebaseAuthenticationFilter
        List<String> excludes = List.of("public", "signup", "login", "docs", "swagger");
        return excludes.stream().anyMatch(path::contains);
    }

    @Bean
    public OpenApiCustomizer customHeaderOpenApiCustomizer() {
        // TODO: Define this list as constant
        return openApi -> {
            openApi.getPaths().forEach((path, pathItem) -> {
                if (!this.shouldNotAddHeader(path)) {
                    pathItem.readOperations().forEach(operation -> {
                        Parameter customHeader = new Parameter()
                                .in("header")
                                .name("amg-community")
                                .description("Community UUID")
                                .required(false)
                                .schema(new StringSchema());
                        operation.addParametersItem(customHeader);
                    });
                }
            });
        };
    }


}
