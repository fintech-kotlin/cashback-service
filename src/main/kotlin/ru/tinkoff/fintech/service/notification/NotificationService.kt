package ru.tinkoff.fintech.service.notification

import ru.tinkoff.fintech.model.NotificationMessageInfo

interface NotificationService {

    fun sendNotification(clientId: String, notificationMessageInfo: NotificationMessageInfo)
}