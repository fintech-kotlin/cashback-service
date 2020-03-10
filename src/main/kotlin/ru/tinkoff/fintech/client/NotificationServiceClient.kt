package ru.tinkoff.fintech.client

interface NotificationServiceClient {

    fun sendNotification(clientId: String, message: String)
}