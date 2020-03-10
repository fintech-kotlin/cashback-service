package ru.tinkoff.fintech.service.notification

import kotlin.math.min

class CardNumberMaskerImpl : CardNumberMasker {

    override fun mask(cardNumber: String, maskChar: Char, start: Int, end: Int): String {
        require(start <= end) { "Start index cannot be greater than end index" }

        val beginIndex = min(start, cardNumber.length)
        val finishIndex = min(end, cardNumber.length)

        return cardNumber.replaceRange(beginIndex, finishIndex, maskChar.toString().repeat(finishIndex - beginIndex))
    }
}