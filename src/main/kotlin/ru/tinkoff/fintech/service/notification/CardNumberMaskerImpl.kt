package ru.tinkoff.fintech.service.notification

class CardNumberMaskerImpl: CardNumberMasker {

    override fun mask(cardNumber: String, maskChar: Char, start: Int, end: Int): String {
        require(end >= start) { "Start index cannot be greater than end index" }

        return cardNumber
            .mapIndexed { index, number -> if (index in start until end) maskChar else number }
            .joinToString("")
    }
}
