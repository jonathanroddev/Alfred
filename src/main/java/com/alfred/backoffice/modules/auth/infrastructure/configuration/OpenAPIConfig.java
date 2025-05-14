package com.alfred.backoffice.modules.auth.infrastructure.configuration;

import com.alfred.backoffice.modules.auth.infrastructure.privacy.PrivacyService;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class OpenAPIConfig {
    private final PrivacyService privacyService;

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    @Bean
    public OpenAPI openAPI() {
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

    @Bean
    public OpenApiCustomizer customHeaderOpenApiCustomizer() {
        return openApi -> {
            openApi.getPaths().forEach((path, pathItem) -> {
                if (!this.privacyService.isUnrestrictedPath(path)) {
                    pathItem.readOperations().forEach(operation -> {
                        Parameter customHeader = new Parameter()
                                .in("header")
                                .name("amg-community")
                                .description("Community UUID")
                                .required(true)
                                .schema(new StringSchema());
                        operation.addParametersItem(customHeader);
                    });
                }
            });
        };
    }


}
