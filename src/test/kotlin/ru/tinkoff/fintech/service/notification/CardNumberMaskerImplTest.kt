package ru.tinkoff.fintech.service.notification

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

private const val CARD_NUMBER = "4155373308145350"

class CardNumberMaskerImplTest {

    private val observable = CardNumberMaskerImpl()

    @Test
    fun `test masking empty card number`() {
        val maskedCardNumber = observable.mask("")
        assertEquals("", maskedCardNumber)
    }

    @Test
    fun `test masking first five digits with #`() {
        val maskedCardNumber = observable.mask(CARD_NUMBER)
        assertEquals("#####73308145350", maskedCardNumber)
    }

    @Test
    fun `test masking five digits in the middle #`() {
        val maskedCardNumber = observable.mask(CARD_NUMBER, start = 5, end = 10)
        assertEquals("41553#####145350", maskedCardNumber)
    }

    @Test
    fun `test masking last five digits with #`() {
        val maskedCardNumber = observable.mask(CARD_NUMBER, start = 11, end = 16)
        assertEquals("41553733081#####", maskedCardNumber)
    }

    @Test
    fun `test masking with last index greater then card number length`() {
        val maskedCardNumber = observable.mask(cardNumber = CARD_NUMBER, end = 100)
        assertEquals("################", maskedCardNumber)
    }

    @Test
    fun `test masking with end index bigger than start index`() {
        assertThrows(java.lang.Exception::class.java,
            {
                observable.mask(
                    cardNumber = CARD_NUMBER,
                    start = 100,
                    end = 4
                )
            },
            "Start index cannot be greater than end index"
        )
    }

    @Test
    fun `test masking length equals zero`() {
        val maskedCardNumber = observable.mask(cardNumber = CARD_NUMBER, start = 4, end = 4)
        assertEquals(CARD_NUMBER, maskedCardNumber)
    }

    @Test
    fun `test masking with start index equal or greater then card number length`() {
        var maskedCardNumber = observable.mask(cardNumber = CARD_NUMBER, start = 20, end = 25)
        assertEquals(CARD_NUMBER, maskedCardNumber)
        maskedCardNumber = observable.mask(cardNumber = CARD_NUMBER, start = 16, end = 20)
        assertEquals(CARD_NUMBER, maskedCardNumber)
    }

    @Test
    fun `test masking last digit with #`() {
        val maskedCardNumber = observable.mask(cardNumber = CARD_NUMBER, start = 15, end = 16)
        assertEquals("415537330814535#", maskedCardNumber)
    }
}
