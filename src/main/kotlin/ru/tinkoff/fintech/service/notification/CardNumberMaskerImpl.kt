package ru.tinkoff.fintech.service.notification

import java.lang.Exception

class CardNumberMaskerImpl: CardNumberMasker {

    override fun mask(cardNumber: String, maskChar: Char, start: Int, end: Int): String {
        if (start > end) throw Exception("Start index cannot be greater than end index")
        if (cardNumber == "" || start == end) return cardNumber
        var endIndex = minOf(end, cardNumber.length)
        return cardNumber.replaceRange(start, endIndex, String.format("%${endIndex - start}s", "").replace(' ', maskChar))
    }
}
