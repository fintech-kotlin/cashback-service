package ru.tinkoff.fintech.db.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "loyalty_payment")
class LoyaltyPaymentEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val value: Double,

    @Column(name = "CARD_ID")
    val cardId: String,

    val sign: String,

    @Column(name = "TRANSACTION_ID")
    val transactionId: String,

    @Column(name = "date_time")
    val dateTime: LocalDateTime
)
