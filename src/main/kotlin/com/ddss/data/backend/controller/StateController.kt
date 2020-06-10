package com.ddss.data.backend.controller

import com.ddss.data.backend.service.StateService
import iot.state.State
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

@RestController
class StateController {

    @Autowired
    private lateinit var stateService: StateService

    /**
     *  Starting point to establish the connection within the Real-Time pipeline
     *
     *  @param host     TaskManager endpoint
     *  @param port     Port listening by TaskManager
     *  @return a ResponseEntity with the value true
     */
    @GetMapping(value = "/init/{host}/{port}")
    fun init(@PathVariable host: String, @PathVariable port: String): ResponseEntity<Mono<Boolean>> {
        val state = stateService.init(host, port)
        return ResponseEntity(true.toMono(), HttpStatus.OK)
    }

    /**
     * Retrieve the state of a given IoT device from a specific job
     *
     * @param jobId     Id of the Flink job
     * @param id        Key to be retrieved from the job
     * @return ResponseEntity with a list of States requested by the Key
     */
    @GetMapping(value = "/state/{jobId}/{id}")
    fun getState(@PathVariable jobId: String, @PathVariable id: String): ResponseEntity<Mono<List<State>?>> {
        val state = stateService.getState(id, jobId)
        return ResponseEntity(state, HttpStatus.OK)
    }

    /**
     * Publish the IoT states into the Real-time pipeline. In the future this will be removed and will be
     * handled by other process
     *
     * @return ResponseEntity with the value true
     */
    @PostMapping(value = "/state")
    fun postState(@RequestBody state: Mono<State>) = ResponseEntity(stateService.register(state), HttpStatus.OK)
}