package ru.tinkoff.fintech.client

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.tinkoff.fintech.model.Card

@Service
class CardServiceClientImpl(
    private val httpClient: HttpClient
) : CardServiceClient {

    @Value("\${paimentprocessing.uri.card-info}")
    private val uri: String? = null;

    override fun getCard(id: String): Card {
        return httpClient.get(uri!!.replace("{id}", id))
    }
}