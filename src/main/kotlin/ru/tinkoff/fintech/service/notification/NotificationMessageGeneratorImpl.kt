package ru.tinkoff.fintech.service.notification

import ru.tinkoff.fintech.model.NotificationMessageInfo

class NotificationMessageGeneratorImpl(
    private val cardNumberMasker: CardNumberMasker
) : NotificationMessageGenerator {

    override fun generateMessage(notificationMessageInfo: NotificationMessageInfo): String =
        """Уважаемый, ${notificationMessageInfo.name}!
        |Спешим Вам сообщить, что на карту ${cardNumberMasker.mask(notificationMessageInfo.cardNumber)}
        |начислен cashback в размере ${notificationMessageInfo.cashback}
        |за категорию ${notificationMessageInfo.category}.
        |Спасибо за покупку ${notificationMessageInfo.transactionDate}""".trimMargin()
}
