package ru.tinkoff.fintech.client

import ru.tinkoff.fintech.model.Client

interface ClientService {

    fun getClient(id: String): Client
}