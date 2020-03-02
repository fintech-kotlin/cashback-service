package ru.tinkoff.fintech.service.cashback.rule

import ru.tinkoff.fintech.model.TransactionInfo
import ru.tinkoff.fintech.service.cashback.LOYALTY_PROGRAM_BLACK
import ru.tinkoff.fintech.service.cashback.util.ZERO
import ru.tinkoff.fintech.service.cashback.util.percentOf

class BlackCashbackRule : CashbackRule {
    override fun apply(transactionInfo: TransactionInfo): Double {
        return if (transactionInfo.loyaltyProgramName == LOYALTY_PROGRAM_BLACK) {
            percentOf(transactionInfo.transactionSum, 1.0)
        } else ZERO
    }
}