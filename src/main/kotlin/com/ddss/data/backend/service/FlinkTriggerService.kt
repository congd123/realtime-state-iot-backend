package com.ddss.data.backend.service

import com.ddss.data.backend.kafka.KafkaProducer
import iot.trigger.Trigger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

@Component
class FlinkTriggerService: TriggerService {

    @Autowired
    private lateinit var producer: KafkaProducer<Trigger>

    override fun register(trigger: Mono<Trigger>): Mono<*> {
        return trigger.subscribe {
            producer.send("devices.trigger", it.getId().toString(), it)
            true
        }.toMono()
    }
}