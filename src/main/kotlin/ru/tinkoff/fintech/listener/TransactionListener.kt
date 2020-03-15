package ru.tinkoff.fintech.listener

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import ru.tinkoff.fintech.model.Transaction
import ru.tinkoff.fintech.service.processor.TransactionProcessor

@Component
class TransactionListener(
    private val transactionProcessor: TransactionProcessor,
    private val objectMapper: ObjectMapper
) {

    @KafkaListener(topics = ["\${spring.kafka.consumer.topic}"])
    fun onMessage(message: String) {
        val transaction = objectMapper.readValue<Transaction>(message, Transaction::class.java)
        transactionProcessor.process(transaction)
    }
}