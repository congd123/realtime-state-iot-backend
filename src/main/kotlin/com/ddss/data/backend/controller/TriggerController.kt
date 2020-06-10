package com.ddss.data.backend.controller

import com.ddss.data.backend.service.TriggerService
import iot.trigger.Trigger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class TriggerController {

    @Autowired
    private lateinit var triggerService: TriggerService

    @PostMapping(value = "/trigger")
    fun postAlert(@RequestBody trigger: Mono<Trigger>) = {
        ResponseEntity(triggerService.register(trigger), HttpStatus.OK)
    }
}