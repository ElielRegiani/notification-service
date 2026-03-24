package whatsapp_platform.notification_service.adapters.out

import jakarta.persistence.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import whatsapp_platform.notification_service.domain.Notification
import whatsapp_platform.notification_service.ports.NotificationRepository
import java.time.LocalDateTime
import java.util.*

@Repository
interface JpaNotificationRepository : JpaRepository<NotificationEntity, UUID> {
    fun findByExternalId(externalId: String): NotificationEntity?
}

@Entity
@Table(name = "notifications")
data class NotificationEntity(
    @Id
    val id: UUID,
    val phone: String,
    val message: String,
    @Enumerated(EnumType.STRING)
    val status: String,
    val createdAt: LocalDateTime,
    val externalId: String?
) {
    companion object {
        fun fromDomain(notification: Notification) = NotificationEntity(
            id = notification.id,
            phone = notification.phone,
            message = notification.message,
            status = notification.status.name,
            createdAt = notification.createdAt,
            externalId = notification.externalId
        )
        fun toDomain(entity: NotificationEntity) = Notification(
            id = entity.id,
            phone = entity.phone,
            message = entity.message,
            status = whatsapp_platform.notification_service.domain.NotificationStatus.valueOf(entity.status),
            createdAt = entity.createdAt,
            externalId = entity.externalId
        )
    }
}

@Component
class NotificationRepositoryAdapter @Autowired constructor(
    private val jpaRepository: JpaNotificationRepository
) : NotificationRepository {
    override fun save(notification: Notification): Notification {
        val entity = NotificationEntity.fromDomain(notification)
        return NotificationEntity.toDomain(jpaRepository.save(entity))
    }
    override fun findByExternalId(externalId: String): Notification? {
        return jpaRepository.findByExternalId(externalId)?.let { NotificationEntity.toDomain(it) }
    }
}
