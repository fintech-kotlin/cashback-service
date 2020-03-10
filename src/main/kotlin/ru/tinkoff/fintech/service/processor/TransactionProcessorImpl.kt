package ru.tinkoff.fintech.service.processor

import org.springframework.stereotype.Service
import ru.tinkoff.fintech.model.Transaction

@Service
class TransactionProcessorImpl : TransactionProcessor {
    override fun process(transaction: Transaction) {
        println("Start process transaction: $transaction")
    }
}