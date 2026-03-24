package whatsapp_platform.notification_service.application

import whatsapp_platform.notification_service.domain.Notification
import whatsapp_platform.notification_service.domain.NotificationStatus
import whatsapp_platform.notification_service.ports.NotificationRepository
import whatsapp_platform.notification_service.ports.EventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class SendNotificationUseCase(
    private val notificationRepository: NotificationRepository,
    private val eventPublisher: EventPublisher
) {
    /**
     * Envia uma notificação, aplicando regras de negócio, persistindo e publicando evento.
     * Garante idempotência via externalId.
     */
    @Transactional
    fun execute(phone: String, message: String, externalId: String?): Notification {
        // Regra: evitar duplicidade (idempotência)
        if (externalId != null) {
            notificationRepository.findByExternalId(externalId)?.let { return it }
        }
        // Cria entidade
        val notification = Notification(
            phone = phone,
            message = message,
            status = NotificationStatus.PENDING,
            externalId = externalId
        )
        // Persiste
        val saved = notificationRepository.save(notification)
        // Publica evento
        eventPublisher.publish(NotificationCreatedEvent.from(saved))
        return saved
    }
}

/**
 * Evento de domínio para publicação no Kafka.
 * Inclui versionamento.
 */
data class NotificationCreatedEvent(
    val id: UUID,
    val phone: String,
    val message: String,
    val status: NotificationStatus,
    val createdAt: String,
    val version: String = "v1"
) {
    companion object {
        fun from(notification: Notification) = NotificationCreatedEvent(
            id = notification.id,
            phone = notification.phone,
            message = notification.message,
            status = notification.status,
            createdAt = notification.createdAt.toString()
        )
    }
}
