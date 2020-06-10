package com.ddss.data.backend.kafka

import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaProducer<T>(private val kafkaTemplate: KafkaTemplate<String, T>) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun send(topic:String, key:String, message: T) {
        kafkaTemplate.send(topic, key, message)
    }
}