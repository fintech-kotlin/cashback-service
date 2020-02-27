package ru.tinkoff.fintech.model

import java.time.LocalDate

data class TransactionInfo(
    val loyaltyProgramName: String,
    val transactionSum: Double,
    val cashbackTotalValue: Double,
    val mccCode: Int? = null,
    val clientBirthDate: LocalDate? = null,
    val firstName: String,
    val lastName: String,
    val middleName: String? = null
)