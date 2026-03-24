package whatsapp_platform.notification_service.domain

import java.time.LocalDateTime
import java.util.*

enum class NotificationStatus {
    PENDING,
    SENT,
    FAILED
}

/**
 * Entidade de domínio principal para notificação.
 * Idempotência pode ser garantida por uma chave externa (ex: externalId).
 */
data class Notification(
    val id: UUID = UUID.randomUUID(),
    val phone: String,
    val message: String,
    val status: NotificationStatus = NotificationStatus.PENDING,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val externalId: String? = null // Para idempotência
)
