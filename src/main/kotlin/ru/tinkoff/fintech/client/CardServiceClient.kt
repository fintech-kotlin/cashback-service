package ru.tinkoff.fintech.client

import ru.tinkoff.fintech.model.Card

interface CardServiceClient {

    fun getCard(id: String): Card
}