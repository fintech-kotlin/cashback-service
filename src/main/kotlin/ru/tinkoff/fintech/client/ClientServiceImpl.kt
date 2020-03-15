package ru.tinkoff.fintech.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import ru.tinkoff.fintech.model.Client

@Service
class ClientServiceImpl(
    private val restTemplate: RestTemplate,
    @Value("\${paimentprocessing.uri.client-info}")
    private val uri: String
) : ClientService {

    override fun getClient(id: String): Client {
        val res = restTemplate.getForEntity("$uri/$id", Client::class.java)
        if (!res.statusCode.is2xxSuccessful) {
            throw RestClientException("Incorrect status: ${res.statusCodeValue}")
        }
        return res.body
    }
}