package org.jpf.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI configuration for working with SwaggerUI.
 */
@RequiredArgsConstructor
@Configuration
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenAPIConfig {
    /**
     * Server URL in Development environment.
     */
    @Value("${app.openapi.dev-url}")
    private String devUrl;

    /**
     * Initiation OpenAPI bean for working with SwaggerUI.
     *
     * @return {@link OpenAPI} with updated parameters.
     */
    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment.");

        Contact contact = new Contact();
        contact.setEmail("aa.sattarov@gmail.com");
        contact.setName("Alexey Sattarov");
        contact.setUrl("http://localhost:8088/api/test/all");

        Info info = new Info()
                .title("Tutorial Hotel Booking API.")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage hotel booking.");

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer));
    }
}
