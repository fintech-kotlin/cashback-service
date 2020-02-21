package ru.tinkoff.fintech.service.notification

import ru.tinkoff.fintech.model.NotificationMessageInfo

class NotificationMessageGeneratorImpl(
    private val cardNumberMasker: CardNumberMasker
) : NotificationMessageGenerator {

    override fun generateMessage(notificationMessageInfo: NotificationMessageInfo): String {
        return "Уважаемый, ${notificationMessageInfo.name}!\n" +
                "Спешим Вам сообщить, что на карту ${cardNumberMasker.mask(notificationMessageInfo.cardNumber)}\n" +
                "начислен cashback в размере ${notificationMessageInfo.cashback}\n" +
                "за категорию ${notificationMessageInfo.category}.\n" +
                "Спасибо за покупку ${notificationMessageInfo.transactionDate}"
    }
}
