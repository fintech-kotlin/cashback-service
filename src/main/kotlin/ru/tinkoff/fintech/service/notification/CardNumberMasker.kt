package ru.tinkoff.fintech.service.notification

interface CardNumberMasker {

    @Throws(Exception::class)
    fun mask(cardNumber: String, maskChar: Char = '#', start: Int = 0, end: Int = 5): String
}