package ru.tinkoff.fintech.service.cashback.rule

import ru.tinkoff.fintech.model.TransactionInfo
import ru.tinkoff.fintech.service.cashback.LOYALTY_PROGRAM_ALL
import ru.tinkoff.fintech.service.cashback.MCC_SOFTWARE
import ru.tinkoff.fintech.service.cashback.util.ZERO
import ru.tinkoff.fintech.service.cashback.util.percentOf

class AllCashbackRule : CashbackRule {
    override fun apply(transactionInfo: TransactionInfo): Double {
        if (transactionInfo.mccCode == MCC_SOFTWARE && transactionInfo.loyaltyProgramName == LOYALTY_PROGRAM_ALL
            && isPalindromeExceptOneSymbol((transactionInfo.transactionSum * 100).toInt())
        ) {
            val lcm = lcm(
                transactionInfo.firstName.length.toDouble(),
                transactionInfo.lastName.length.toDouble()
            )

            return percentOf(transactionInfo.transactionSum, lcm * 0.001)
        }
        return ZERO
    }

    private fun isPalindromeExceptOneSymbol(num: Int): Boolean {
        val string = num.toString()
        var changes = 0

        for (i in string.length - 1 downTo 0) {
            if (string[i] != string[string.length - 1 - i]) {
                changes++
            }
        }

        return changes.div(2) < 2
    }

    private fun gcd(a: Int, b: Int): Int {
        return if (b == 0) a else gcd(b, a % b)
    }

    private fun lcm(a: Double, b: Double): Double {
        return a / gcd(a.toInt(), b.toInt()) * b
    }
}