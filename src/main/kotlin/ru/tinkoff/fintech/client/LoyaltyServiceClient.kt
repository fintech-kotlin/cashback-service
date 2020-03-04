package ru.tinkoff.fintech.client

import ru.tinkoff.fintech.model.LoyaltyProgram

interface LoyaltyServiceClient {

    fun getLoyaltyProgram(id: String): LoyaltyProgram
}