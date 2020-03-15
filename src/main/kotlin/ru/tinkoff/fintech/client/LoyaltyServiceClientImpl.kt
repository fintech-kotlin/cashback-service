package ru.tinkoff.fintech.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import ru.tinkoff.fintech.model.LoyaltyProgram

@Service
class LoyaltyServiceClientImpl(
    private val restTemplate: RestTemplate,
    @Value("\${paimentprocessing.uri.loyalty-info}")
    private val uri: String
) : LoyaltyServiceClient {

    override fun getLoyaltyProgram(id: String): LoyaltyProgram {
        val res = restTemplate.getForEntity("$uri/$id", LoyaltyProgram::class.java)
        if (!res.statusCode.is2xxSuccessful) {
            throw RestClientException("Incorrect status: ${res.statusCodeValue}")
        }
        return res.body
    }
}