package ru.tinkoff.fintech.service.cashback

import ru.tinkoff.fintech.model.TransactionInfo
import kotlin.math.round

internal const val LOYALTY_PROGRAM_BLACK = "BLACK"
internal const val LOYALTY_PROGRAM_ALL = "ALL"
internal const val LOYALTY_PROGRAM_BEER = "BEER"
internal const val MAX_CASH_BACK = 3000.0
internal const val MCC_SOFTWARE = 5734
internal const val MCC_BEER = 5921

class CashbackCalculatorImpl(
    private val cashbackRules: CashbackRules
) : CashbackCalculator {

    override fun calculateCashback(transactionInfo: TransactionInfo): Double {
        var cashbackValue = 0.00
        when (transactionInfo.loyaltyProgramName) {
            LOYALTY_PROGRAM_BLACK -> cashbackValue = cashbackRules.loyaltyBlack(transactionInfo)
            LOYALTY_PROGRAM_ALL ->
                if (transactionInfo.mccCode == MCC_SOFTWARE) cashbackValue = cashbackRules.loyaltyAllMccSoftware(transactionInfo)
            LOYALTY_PROGRAM_BEER ->
                if (transactionInfo.mccCode == MCC_BEER) cashbackValue = cashbackRules.loyaltyBeerMccBeer(transactionInfo)
        }
        cashbackValue += cashbackRules.additionIfAmountMultipleOfNumber(transactionInfo.transactionSum, 666)
        return (round(cashbackValue * 100) / 100).coerceAtMost(MAX_CASH_BACK - transactionInfo.cashbackTotalValue)
    }
}

