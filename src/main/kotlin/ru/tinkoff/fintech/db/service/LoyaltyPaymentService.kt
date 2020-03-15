package ru.tinkoff.fintech.db.service

import org.springframework.stereotype.Service
import ru.tinkoff.fintech.db.entity.LoyaltyPaymentEntity
import ru.tinkoff.fintech.db.repository.LoyaltyPaymentRepository
import java.time.YearMonth

@Service
class LoyaltyPaymentService(
    private val loyaltyPaymentRepository: LoyaltyPaymentRepository
) {

    fun save(loyaltyPayment: LoyaltyPaymentEntity) {
        loyaltyPaymentRepository.save(loyaltyPayment)
    }

    fun getTotalMonthCashback(cardId: String, sign: String): Double {
        val payments = loyaltyPaymentRepository.findAllByCardIdAndSignAndDateTimeAfter(
            cardId,
            sign,
            YearMonth.now().atDay(1).atStartOfDay()
        )

        return payments.fold(0.0, { acc, lp -> acc + lp.value })
    }
}