package ru.tinkoff.fintech.service.cashback

import ru.tinkoff.fintech.model.TransactionInfo
import java.time.LocalDate

class CashbackRulesImpl : CashbackRules {

    override fun loyaltyBlack(transactionInfo: TransactionInfo): Double = transactionInfo.transactionSum * 0.01

    override fun additionIfAmountMultipleOfNumber(amount: Double, number: Int): Double =
        if (amount % number == 0.00) number / 100.00 else 0.00

    override fun loyaltyAllMccSoftware(transactionInfo: TransactionInfo): Double {
        val palindrome = (transactionInfo.transactionSum * 100).toLong().toString()
        var numOfMistakes = 0
        for (i in 0 until palindrome.length / 2) {
            if (palindrome[i] != palindrome[palindrome.length - i - 1]) numOfMistakes++
        }
        if (numOfMistakes <= 1) {
            return transactionInfo.transactionSum * nok(
                transactionInfo.firstName.length,
                transactionInfo.lastName.length
            ) / 1000 / 100
        }
        return 0.00
    }

    private fun nok(x: Int, y: Int): Int = x * y / nod(x, y)

    private fun nod(x: Int, y: Int): Int {
        val tmp = x % y
        return if (tmp > 0) nod(y, tmp) else y
    }

    override fun loyaltyBeerMccBeer(
        transactionInfo: TransactionInfo,
        firstNameStr: String,
        lastNameStr: String
    ): Double {
        with(transactionInfo) {
            val monthFirstLetterVector = "#яфмамииасонд"
            val date = LocalDate.now()
            val cashbackPercent: Double =
                if (firstName.toLowerCase() == firstNameStr.toLowerCase())
                    if (lastName.toLowerCase() == lastNameStr.toLowerCase()) (10.0 / 100) else (7.0 / 100)
                else if (monthFirstLetterVector[date.monthValue] == firstName[0]) (5.0 / 100)
                else if (monthFirstLetterVector[date.minusMonths(1).monthValue] == firstName[0]) (3.0 / 100)
                else if (monthFirstLetterVector[date.plusMonths(1).monthValue] == firstName[0]) (3.0 / 100)
                else (2.0 / 100)
            return transactionSum * cashbackPercent
        }
    }
}
