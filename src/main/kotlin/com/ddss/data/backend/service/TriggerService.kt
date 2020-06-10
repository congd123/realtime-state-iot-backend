package com.ddss.data.backend.service

import iot.trigger.Trigger
import reactor.core.publisher.Mono

/**
 * Service to handle with the triggers
 */
interface TriggerService {
    /**
     * Create a new trigger
     *
     * @param trigger   Trigger to add
     */
    fun register(trigger: Mono<Trigger>): Mono<*>
}