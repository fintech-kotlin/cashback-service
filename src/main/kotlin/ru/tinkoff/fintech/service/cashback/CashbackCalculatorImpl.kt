package ru.tinkoff.fintech.service.cashback

import ru.tinkoff.fintech.model.TransactionInfo
import ru.tinkoff.fintech.service.cashback.rule.AllCashbackRule
import ru.tinkoff.fintech.service.cashback.rule.BeerCashbackRule
import ru.tinkoff.fintech.service.cashback.rule.BlackCashbackRule
import ru.tinkoff.fintech.service.cashback.rule.CashbackRule666
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.min

internal const val LOYALTY_PROGRAM_BLACK = "BLACK"
internal const val LOYALTY_PROGRAM_ALL = "ALL"
internal const val LOYALTY_PROGRAM_BEER = "BEER"
internal const val MAX_CASH_BACK = 3000.0
internal const val MCC_SOFTWARE = 5734
internal const val MCC_BEER = 5921

private val CASHBACK_RULES = listOf(
    AllCashbackRule(),
    BeerCashbackRule(),
    BlackCashbackRule(),
    CashbackRule666()
)

class CashbackCalculatorImpl : CashbackCalculator {

    override fun calculateCashback(transactionInfo: TransactionInfo): Double {
        val totalCashback = CASHBACK_RULES.sumByDouble { rule -> rule.apply(transactionInfo) }
        val roundedCashback = BigDecimal(totalCashback).setScale(2, RoundingMode.HALF_DOWN).toDouble()
        return min(roundedCashback, MAX_CASH_BACK - transactionInfo.cashbackTotalValue)
    }

}