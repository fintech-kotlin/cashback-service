package ru.tinkoff.fintech.service.notification

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.tinkoff.fintech.model.NotificationMessageInfo
import java.time.LocalDateTime

class NotificationMessageGeneratorImplTest {

    private val cardNumberStub = mock<CardNumberMasker> {
        on { mask(any(), any(), any(), any()) } doReturn "#####"
    }
    private val messageGenerator = NotificationMessageGeneratorImpl(cardNumberStub)

    @Test
    fun `test generating message`() {
        val messageInfo = createNotificationMessageInfo()
        val message = messageGenerator.generateMessage(messageInfo)
        assertEquals(
            "Уважаемый, Сергей!\n" +
            "Спешим Вам сообщить, что на карту #####\n" +
            "начислен cashback в размере 54.45\n" +
            "за категорию BLACK.\n" +
            "Спасибо за покупку 2020-11-20T01:00".trimIndent(), message)
    }

    private fun createNotificationMessageInfo() =
        NotificationMessageInfo(
            name = "Сергей",
            cardNumber = "4716873837253400",
            cashback = 54.45,
            transactionSum = 62346.33,
            category = "BLACK",
            transactionDate = LocalDateTime.of(2020, 11, 20, 1, 0)
        )
}