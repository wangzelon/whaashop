package com.whaa.shop.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Boot 4 uses Jackson 3 for its primary JSON infrastructure. Some
 * integrations in this project still expose Jackson 2 types, so keep this
 * narrowly scoped compatibility mapper until those APIs migrate.
 */
@Configuration
public class JacksonCompatibilityConfig {
    @Bean
    ObjectMapper jackson2ObjectMapper() {
        return JsonMapper.builder().findAndAddModules().build();
    }
}
