package ru.tinkoff.fintech.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

@Configuration
open class BeanDefinition {
    @Bean
    open fun restTemplate(objectMapper: ObjectMapper): RestTemplate {
        return RestTemplateBuilder()
            .messageConverters(MappingJackson2HttpMessageConverter(objectMapper))
            .build();
    }

    @Bean
    open fun objectMapper(): ObjectMapper {
        return ObjectMapper()
            .registerModules(KotlinModule(), JavaTimeModule())
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
//            .setDateFormat(StdDateFormat())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }
}