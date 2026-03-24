package whatsapp_platform.notification_service.ports

import whatsapp_platform.notification_service.domain.Notification

interface NotificationRepository {
    fun save(notification: Notification): Notification
    fun findByExternalId(externalId: String): Notification?
}

interface EventPublisher {
    fun publish(event: Any)
}
