package ru.tinkoff.fintech.service.notification

class CardNumberMaskerImpl : CardNumberMasker {

    override fun mask(cardNumber: String, maskChar: Char, start: Int, end: Int): String {
        require(start <= end) { "Start index cannot be greater than end index" }
        if (cardNumber == "" || start == end || start >= cardNumber.length) return cardNumber
        val endIndex = minOf(end, cardNumber.length)
        return cardNumber.replaceRange(start, endIndex, maskChar.toString().repeat(endIndex - start))
    }
}
