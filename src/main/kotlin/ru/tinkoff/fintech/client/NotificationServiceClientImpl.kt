package ru.tinkoff.fintech.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class NotificationServiceClientImpl(
    private val restTemplate: RestTemplate,
    @Value("\${paimentprocessing.uri.notification}")
    private val uri: String
) : NotificationServiceClient {

    override fun sendNotification(clientId: String, message: String) {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON_UTF8

        val res = restTemplate.postForEntity("$uri/$clientId/message", HttpEntity(message, headers), String::class.java)
        if (!res.statusCode.is2xxSuccessful) {
            println("Unexpected status: $res.statusCodeValue")
        }
    }
}