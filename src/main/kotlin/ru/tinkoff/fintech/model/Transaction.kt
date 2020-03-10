package ru.tinkoff.fintech.model

import java.time.LocalDateTime

data class Transaction(
    val transactionId: String,
    val time: LocalDateTime,
    val cardNumber: String,
    val operationType: Int,
    val value: Double,
    val currencyCode: String,
    val mccCode: Int?
)
