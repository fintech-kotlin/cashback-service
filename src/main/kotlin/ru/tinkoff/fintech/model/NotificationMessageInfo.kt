package ru.tinkoff.fintech.model

import java.time.LocalDateTime

data class NotificationMessageInfo(
    val name: String,
    val cardNumber: String,
    val cashback: Double,
    val transactionSum: Double,
    val category: String,
    val transactionDate: LocalDateTime
)