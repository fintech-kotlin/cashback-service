package ru.tinkoff.fintech.db.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.tinkoff.fintech.db.entity.LoyaltyPaymentEntity


interface LoyaltyPaymentRepository : JpaRepository<LoyaltyPaymentEntity, Long>
