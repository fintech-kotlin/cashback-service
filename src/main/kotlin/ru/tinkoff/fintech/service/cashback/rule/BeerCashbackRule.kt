package ru.tinkoff.fintech.service.cashback.rule

import ru.tinkoff.fintech.model.TransactionInfo
import ru.tinkoff.fintech.service.cashback.LOYALTY_PROGRAM_BEER
import ru.tinkoff.fintech.service.cashback.MCC_BEER
import ru.tinkoff.fintech.service.cashback.util.ZERO
import ru.tinkoff.fintech.service.cashback.util.percentOf
import java.text.DateFormatSymbols
import java.time.LocalDate
import java.time.Month
import java.util.*

private const val FIRST_NAME = "Олег"
private const val LAST_NAME = "Олегов"

class BeerCashbackRule : CashbackRule {
    override fun apply(transactionInfo: TransactionInfo): Double {
        if (transactionInfo.mccCode == MCC_BEER && transactionInfo.loyaltyProgramName == LOYALTY_PROGRAM_BEER) {
            if (transactionInfo.firstName.equals(FIRST_NAME, true)) {
                return if (transactionInfo.lastName.equals(LAST_NAME, true))
                    percentOf(transactionInfo.transactionSum, 10.0)
                else percentOf(transactionInfo.transactionSum, 7.0)
            }

            val firstLetter = transactionInfo.firstName[0]
            val now = LocalDate.now()

            if (firstLettersOfNameAndMonthAreEqual(firstLetter, now.month)) {
                return percentOf(transactionInfo.transactionSum, 5.0)
            }

            if (firstLettersOfNameAndMonthAreEqual(firstLetter, now.minusMonths(1).month) ||
                firstLettersOfNameAndMonthAreEqual(firstLetter, now.plusMonths(1).month)
            ) {
                return percentOf(transactionInfo.transactionSum, 3.0)
            }

            return percentOf(transactionInfo.transactionSum, 2.0)
        }
        return ZERO
    }

    private fun firstLettersOfNameAndMonthAreEqual(letter: Char, month: Month): Boolean {
        return getMonthName(month.value)[0].equals(letter, true)
    }

    private fun getMonthName(month: Int): String {
        val symbols = DateFormatSymbols(Locale("RU"))
        return symbols.months[month - 1]
    }
}