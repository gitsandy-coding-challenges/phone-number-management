package com.telecom.phone.number.management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for customizing the OpenAPI documentation.
 *
 * <p>This class provides configuration for generating the OpenAPI specification
 * for the Telecom Phone Number Management API. It defines the metadata such as
 * title, version, and description for the API documentation.</p>
 *
 * <p>The generated OpenAPI documentation can be accessed through the Swagger UI
 * or other tools that support OpenAPI specifications.</p>
 *
 * @author Sandeep
 * @version 1.0
 * @since 2025-01-27
 */
@Configuration
public class OpenApiConfig {

    /**
     * Creates and configures a custom OpenAPI bean.
     *
     * <p>The OpenAPI instance is configured with metadata, including the API title,
     * version, and description. This metadata is displayed in the Swagger UI and
     * other OpenAPI-compatible tools.</p>
     *
     * @return A configured instance of {@link OpenAPI}.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Telecom Phone Number Management API")
                .version("1.0.0")
                .description("API for managing phone numbers associated with customers. Handles new and existing phone numbers with activation support."));
    }
}