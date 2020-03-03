package ru.tinkoff.fintech.service.cashback

import ru.tinkoff.fintech.model.TransactionInfo

interface CashbackRules {

    fun loyaltyBlack(transactionInfo: TransactionInfo): Double

    fun additionIfAmountMultipleOfNumber(amount: Double, number: Int): Double

    fun loyaltyAllMccSoftware(transactionInfo: TransactionInfo): Double

    fun loyaltyBeerMccBeer(transactionInfo: TransactionInfo,
                           name: String = "Олег",
                           lastname: String = "Олегов"
    ): Double
}
