package ru.tinkoff.fintech

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.util.ISO8601DateFormat
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
open class Application

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}

@Bean
fun httpClient(objectMapper: ObjectMapper): HttpClient {
    return HttpClient(Apache) {
        install(JsonFeature) {
            serializer = JacksonSerializer(objectMapper)
        }
    }
}

@Bean
fun objectMapper(): ObjectMapper {
    return ObjectMapper()
        .registerModules(KotlinModule(), JavaTimeModule())
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .setDateFormat(ISO8601DateFormat())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
}