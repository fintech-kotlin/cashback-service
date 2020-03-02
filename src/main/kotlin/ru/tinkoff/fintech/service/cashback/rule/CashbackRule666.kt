package ru.tinkoff.fintech.service.cashback.rule

import ru.tinkoff.fintech.model.TransactionInfo
import ru.tinkoff.fintech.service.cashback.util.DEVIL_NUMBER
import ru.tinkoff.fintech.service.cashback.util.ZERO

class CashbackRule666 : CashbackRule {
    override fun apply(transactionInfo: TransactionInfo): Double {
        return if (transactionInfo.transactionSum % 666 == 0.0) {
            DEVIL_NUMBER
        } else ZERO
    }
}