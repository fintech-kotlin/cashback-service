package ru.tinkoff.fintech.service.cashback

import ru.tinkoff.fintech.model.TransactionInfo

interface CashbackCalculator {

    fun calculateCashback(transactionInfo: TransactionInfo): Double
}