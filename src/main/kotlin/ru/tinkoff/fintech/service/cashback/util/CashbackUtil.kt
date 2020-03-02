package ru.tinkoff.fintech.service.cashback.util

/**
 * Нулевое значение
 */
const val ZERO = 0.0

/**
 * 😈
 */
const val DEVIL_NUMBER = 6.66

/**
 * Утилитный метод для расчёта процентной части от числа
 * @param number  - число
 * @param percent - процент, который необходимо взять от числа
 */
fun percentOf(number: Double, percent: Double): Double {
    return (number / 100) * percent
}