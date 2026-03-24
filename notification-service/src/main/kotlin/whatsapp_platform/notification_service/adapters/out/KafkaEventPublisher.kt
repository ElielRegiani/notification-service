package whatsapp_platform.notification_service.adapters.out

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import whatsapp_platform.notification_service.ports.EventPublisher

@Component
class KafkaEventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, Any>,
    @Value("\${notification.kafka.topic:notification.created}")
    private val topic: String
) : EventPublisher {
    private val logger = LoggerFactory.getLogger(KafkaEventPublisher::class.java)

    override fun publish(event: Any) {
        logger.info("Publishing event to Kafka: {}", event)
        kafkaTemplate.send(topic, event)
    }
}
