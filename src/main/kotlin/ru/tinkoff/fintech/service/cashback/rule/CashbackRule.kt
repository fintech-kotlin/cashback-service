package ru.tinkoff.fintech.service.cashback.rule

import ru.tinkoff.fintech.model.TransactionInfo

interface CashbackRule {
    fun apply(transactionInfo: TransactionInfo): Double
}