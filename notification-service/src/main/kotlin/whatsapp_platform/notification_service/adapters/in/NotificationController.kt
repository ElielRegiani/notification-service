package whatsapp_platform.notification_service.adapters.`in`

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import whatsapp_platform.notification_service.application.SendNotificationUseCase

@RestController
@RequestMapping("/internal/notifications")
class NotificationController(
    private val sendNotificationUseCase: SendNotificationUseCase
) {
    @PostMapping
    fun sendNotification(@RequestBody request: SendNotificationRequest): ResponseEntity<Any> {
        val notification = sendNotificationUseCase.execute(
            phone = request.phone,
            message = request.message,
            externalId = request.externalId
        )
        return ResponseEntity.ok(notification)
    }
}

data class SendNotificationRequest(
    val phone: String,
    val message: String,
    val externalId: String? = null // Para idempotência
)
