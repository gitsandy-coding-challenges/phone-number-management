package com.telecom.phone.number.management.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for customizing the Jackson ObjectMapper.
 *
 * <p>This configuration ensures that only non-null fields are included
 * during serialization, optimizing the JSON payload for REST APIs.</p>
 *
 * <p>The {@link ObjectMapper} bean created here will be used globally across
 * the application wherever Jackson serialization or deserialization is required.</p>
 *
 * @author Sandeep
 * @version 1.0
 * @since 2025-01-27
 */
@Configuration
public class JacksonConfig {

    /**
     * Creates and configures a Jackson {@link ObjectMapper} bean.
     *
     * <p>The ObjectMapper is customized to include only non-null fields
     * in the serialized JSON output by setting the serialization inclusion
     * to {@code JsonInclude.Include.NON_NULL}.</p>
     *
     * @return A configured instance of {@link ObjectMapper}.
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }
}