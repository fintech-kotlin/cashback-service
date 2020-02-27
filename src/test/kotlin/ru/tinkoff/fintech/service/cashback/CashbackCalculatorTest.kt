package ru.tinkoff.fintech.service.cashback

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.tinkoff.fintech.model.TransactionInfo
import java.time.LocalDate
import java.time.Month

class CashbackCalculatorTest {

    private val observable = CashbackCalculatorImpl()

    @Test
    fun `cashBack is equals 10 for first transaction sum 1000 and black loyalty program`() {
        val transactionInfo = TransactionInfo(
            loyaltyProgramName = LOYALTY_PROGRAM_BLACK,
            transactionSum = 1000.0,
            cashbackTotalValue = 0.0,
            firstName = "testFirstName",
            lastName = "testLastName"
        )
        val cashbackSum = observable.calculateCashback(transactionInfo)
        assertEquals(10.0, cashbackSum)
    }

    @Test
    fun `cashBack is equals 0 if cash back total value equals 3000`() {
        val transactionInfo = TransactionInfo(
            loyaltyProgramName = LOYALTY_PROGRAM_BLACK,
            transactionSum = 1000.0,
            cashbackTotalValue = MAX_CASH_BACK,
            firstName = "testFirstName",
            lastName = "testLastName"
        )
        val cashbackSum = observable.calculateCashback(transactionInfo)
        assertEquals(0.0, cashbackSum)
    }

    @Test
    fun `cash back is equals 500 if transaction sum equals 100_000 and cash back total value equals 2500`() {
        val transactionInfo = TransactionInfo(
            loyaltyProgramName = LOYALTY_PROGRAM_BLACK,
            transactionSum = 100_000.0,
            cashbackTotalValue = 2500.0,
            firstName = "testFirstName",
            lastName = "testLastName"
        )
        val cashbackSum = observable.calculateCashback(transactionInfo)
        assertEquals(500.0, cashbackSum)
    }

    @Test
    fun `cash back for sums divisible on 666 increases on 6,66`() {
        val transactionInfo = TransactionInfo(
            loyaltyProgramName = LOYALTY_PROGRAM_BLACK,
            transactionSum = 666.0,
            cashbackTotalValue = 1000.0,
            firstName = "testFirstName",
            lastName = "testLastName"
        )
        val cashbackSum = observable.calculateCashback(transactionInfo)
        assertEquals(13.32, cashbackSum)
    }


    @Test
    fun `cash back for palindrome sums with one replace and mcc = 5734 and loyalty ALL`() {
        val transactionInfo = TransactionInfo(
            loyaltyProgramName = LOYALTY_PROGRAM_ALL,
            transactionSum = 100_000.0,
            cashbackTotalValue = 0.0,
            firstName = "Ева",
            lastName = "Охоцимская",
            mccCode = MCC_SOFTWARE
        )
        val cashbackSum = observable.calculateCashback(transactionInfo)
        assertEquals(30.0, cashbackSum)
    }

    @Test
    fun `cash back for palindrome sums with one replace and mcc = 5734 and loyalty ALL 2`() {
        val transactionInfo = TransactionInfo(
            loyaltyProgramName = LOYALTY_PROGRAM_ALL,
            transactionSum = 10_000.0,
            cashbackTotalValue = 0.0,
            firstName = "Дмитрий",
            lastName = "Масалимов",
            mccCode = MCC_SOFTWARE
        )
        val cashbackSum = observable.calculateCashback(transactionInfo)
        assertEquals(6.3, cashbackSum)
    }

    @Test
    fun `cash back for palindrome sums with one replace and mcc = 5734 and loyalty ALL 3`() {
        val transactionInfo = TransactionInfo(
            loyaltyProgramName = LOYALTY_PROGRAM_ALL,
            transactionSum = 50_000.0,
            cashbackTotalValue = 2500.0,
            firstName = "Лиза",
            lastName = "Кразаева",
            mccCode = MCC_SOFTWARE
        )
        val cashbackSum = observable.calculateCashback(transactionInfo)
        assertEquals(4.0, cashbackSum)
    }

    @Test
    fun `cash back for palindrome sums with one replace and mcc = 5734 and loyalty ALL 4`() {
        val transactionInfo = TransactionInfo(
            loyaltyProgramName = LOYALTY_PROGRAM_ALL,
            transactionSum = 12_222.20,
            cashbackTotalValue = 2000.0,
            firstName = "Валентина",
            lastName = "Клинешнева",
            mccCode = MCC_SOFTWARE
        )
        val cashbackSum = observable.calculateCashback(transactionInfo)
        assertEquals(11.0, cashbackSum)
    }

    @Test
    fun `cash back for palindrome sums with one replace and mcc = 5734 and loyalty ALL limited by max cash back`() {
        val transactionInfo = TransactionInfo(
            loyaltyProgramName = LOYALTY_PROGRAM_ALL,
            transactionSum = 100_000.0,
            cashbackTotalValue = 0.0,
            firstName = "Дмитрий",
            lastName = "Масалимов",
            mccCode = MCC_SOFTWARE
        )
        val cashbackSum = observable.calculateCashback(transactionInfo)
        assertEquals(63.0, cashbackSum)
    }

    @Test
    fun `cash back for palindrome sums with one replace with random mcc and loyalty ALL limited by max cash back`() {
        val transactionInfo = TransactionInfo(
            loyaltyProgramName = LOYALTY_PROGRAM_ALL,
            transactionSum = 50_000.0,
            cashbackTotalValue = 2500.0,
            firstName = "Лиза",
            lastName = "Кразаева",
            mccCode = 1234
        )
        val cashbackSum = observable.calculateCashback(transactionInfo)
        assertEquals(0.0, cashbackSum)
    }

    @Test
    fun `BEER cashback for name = Олег with any case`() {
        val transactionInfo = TransactionInfo(
            loyaltyProgramName = LOYALTY_PROGRAM_BEER,
            transactionSum = 5000.0,
            cashbackTotalValue = 2500.0,
            firstName = "ОлЕг",
            lastName = "Кразаева",
            mccCode = MCC_BEER
        )
        val cashbackSum = observable.calculateCashback(transactionInfo)
        assertEquals(350.0, cashbackSum)
    }

    @Test
    fun `BEER cashback for name = Олег and lastName = Олегов with any case`() {
        val transactionInfo = TransactionInfo(
            loyaltyProgramName = LOYALTY_PROGRAM_BEER,
            transactionSum = 5000.0,
            cashbackTotalValue = 2500.0,
            firstName = "ОлеГ",
            lastName = "ОлЕгОВ",
            mccCode = MCC_BEER
        )
        val cashbackSum = observable.calculateCashback(transactionInfo)
        assertEquals(500.0, cashbackSum)
    }

    @Test
    fun `BEER cashback for firstName first letter equals month first letter`() {
        val transactionInfo = TransactionInfo(
            loyaltyProgramName = LOYALTY_PROGRAM_BEER,
            transactionSum = 5000.0,
            cashbackTotalValue = 2500.0,
            firstName = monthWithFirstLetter[LocalDate.now().month.value].toString(),
            lastName = "ОЛЕГОВ",
            mccCode = MCC_BEER
        )
        val cashbackSum = observable.calculateCashback(transactionInfo)
        assertEquals(250.0, cashbackSum)
    }

    @Test
    fun `BEER cashback for firstName first letter equals previous month first letter`() {
        val transactionInfo = TransactionInfo(
            loyaltyProgramName = LOYALTY_PROGRAM_BEER,
            transactionSum = 5000.0,
            cashbackTotalValue = 2500.0,
            firstName = monthWithFirstLetter[LocalDate.now().minusMonths(1).month.value].toString(),
            lastName = "ОЛЕГОВ",
            mccCode = MCC_BEER
        )
        val cashbackSum = observable.calculateCashback(transactionInfo)
        assertEquals(150.0, cashbackSum)
    }

    @Test
    fun `BEER cashback for firstName first letter equals next month first letter`() {
        val transactionInfo = TransactionInfo(
            loyaltyProgramName = LOYALTY_PROGRAM_BEER,
            transactionSum = 5000.0,
            cashbackTotalValue = 2500.0,
            firstName = monthWithFirstLetter[LocalDate.now().plusMonths(1).month.value].toString(),
            lastName = "ОЛЕГОВ",
            mccCode = MCC_BEER
        )
        val cashbackSum = observable.calculateCashback(transactionInfo)
        assertEquals(150.0, cashbackSum)
    }

    val monthWithFirstLetter = mapOf(
        Month.JANUARY.value to 'я',
        Month.FEBRUARY.value to 'ф',
        Month.MARCH.value to 'м',
        Month.APRIL.value to 'а',
        Month.MAY.value to 'м',
        Month.JUNE.value to 'и',
        Month.JULY.value to 'и',
        Month.AUGUST.value to 'а',
        Month.SEPTEMBER.value to 'с',
        Month.OCTOBER.value to 'о',
        Month.NOVEMBER.value to 'н',
        Month.DECEMBER.value to 'д'
    )

}