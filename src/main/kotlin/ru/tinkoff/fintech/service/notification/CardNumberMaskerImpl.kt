package ru.tinkoff.fintech.service.notification

class CardNumberMaskerImpl : CardNumberMasker {

    override fun mask(cardNumber: String, maskChar: Char, start: Int, end: Int): String {
        if (cardNumber.isEmpty() || start == end) {
            return cardNumber
        }

        if (start > end) {
            throw Exception("Start index cannot be greater than end index")
        }

        val range = if (end > cardNumber.length) cardNumber.length else end
        return cardNumber.replaceRange(start, range, maskChar.toString().repeat(range - start))
    }
}