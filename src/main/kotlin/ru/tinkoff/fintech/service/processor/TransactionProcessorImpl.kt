package ru.tinkoff.fintech.service.processor

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.tinkoff.fintech.client.CardServiceClient
import ru.tinkoff.fintech.client.ClientService
import ru.tinkoff.fintech.client.LoyaltyServiceClient
import ru.tinkoff.fintech.client.NotificationServiceClient
import ru.tinkoff.fintech.db.entity.LoyaltyPaymentEntity
import ru.tinkoff.fintech.db.service.LoyaltyPaymentService
import ru.tinkoff.fintech.model.*
import ru.tinkoff.fintech.service.cashback.CashbackCalculator
import ru.tinkoff.fintech.service.notification.NotificationMessageGenerator

@Service
class TransactionProcessorImpl(
    private val cardServiceClient: CardServiceClient,
    private val clientService: ClientService,
    private val loyaltyServiceClient: LoyaltyServiceClient,
    private val notificationServiceClient: NotificationServiceClient,
    private val cashbackCalculator: CashbackCalculator,
    private val loyaltyPaymentService: LoyaltyPaymentService,
    private val notificationMessageGenerator: NotificationMessageGenerator,
    @Value("\${paimentprocessing.sign}")
    private val sign: String
) : TransactionProcessor {

    override fun process(transaction: Transaction) {
        println("Start process transaction: $transaction")
        if (transaction.mccCode == null) {
            println("Cashback will not be calculated due to absence MCC in transaction: $transaction")
            return
        }

        val card = cardServiceClient.getCard(transaction.cardNumber)
        val client = clientService.getClient(card.client)
        val loyaltyProgram = loyaltyServiceClient.getLoyaltyProgram(card.loyaltyProgram)

        val transactionInfo = createTransactionInfo(
            loyaltyProgram,
            transaction,
            client,
            loyaltyPaymentService.getTotalMonthCashback(card.id, sign)
        )

        val cashback = cashbackCalculator.calculateCashback(transactionInfo)

        loyaltyPaymentService.save(createLoyaltyPayment(cashback, card.id, transaction))

        notificationServiceClient.sendNotification(
            client.id,
            notificationMessageGenerator.generateMessage(
                createNotificationMessageInfo(
                    transaction,
                    transactionInfo,
                    cashback
                )
            )
        )

        println("Transaction has been processed: $transaction")
    }

    private fun createTransactionInfo(
        loyaltyProgram: LoyaltyProgram,
        transaction: Transaction,
        client: Client,
        cashbackTotalValue: Double
    ) =
        TransactionInfo(
            loyaltyProgramName = loyaltyProgram.name,
            transactionSum = transaction.value,
            cashbackTotalValue = cashbackTotalValue,
            mccCode = transaction.mccCode,
            clientBirthDate = client.birthDate?.toLocalDate(),
            firstName = client.firstName!!,
            lastName = client.lastName!!,
            middleName = client.middleName!!
        )

    private fun createLoyaltyPayment(cashback: Double, cardId: String, transaction: Transaction) =
        LoyaltyPaymentEntity(
            value = cashback,
            cardId = cardId,
            sign = sign,
            transactionId = transaction.transactionId,
            dateTime = transaction.time
        )

    private fun createNotificationMessageInfo(
        transaction: Transaction,
        transactionInfo: TransactionInfo,
        cashback: Double
    ) =
        NotificationMessageInfo(
            name = transactionInfo.firstName,
            cardNumber = transaction.cardNumber,
            cashback = cashback,
            transactionSum = transactionInfo.transactionSum,
            category = transactionInfo.loyaltyProgramName,
            transactionDate = transaction.time
        )
}