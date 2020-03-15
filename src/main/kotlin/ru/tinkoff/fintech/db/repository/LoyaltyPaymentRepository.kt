package ru.tinkoff.fintech.db.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.tinkoff.fintech.db.entity.LoyaltyPaymentEntity
import java.time.LocalDateTime


interface LoyaltyPaymentRepository : JpaRepository<LoyaltyPaymentEntity, Long> {
    fun findAllByCardIdAndSignAndDateTimeAfter(
        cardId: String,
        sign: String,
        dateTime: LocalDateTime
    ): List<LoyaltyPaymentEntity>
}