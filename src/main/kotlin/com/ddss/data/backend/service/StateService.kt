package com.ddss.data.backend.service

import iot.state.State
import reactor.core.publisher.Mono

/**
 * Contract to interact with the state in the Real-time pipeline
 */
interface StateService {
    /**
     * Establish the connection with the data pipeline
     *
     * @param host  Data pipeline endpoint
     * @param port  Port listening by the data pipeline
     * @return a Boolean in case the connection was established
     */
    fun init(host: String, port: String): Mono<Boolean?>

    /**
     * Retrieve state from the data pipeline
     *
     * @param id    Resource that is to know the current state
     * @param jobId JobId that is running in the data pipeline
     * @return a List<State> with all of the states from that job and id
     */
    fun getState(id: String, jobId: String): Mono<List<State>?>

    /**
     * Create a new state
     *
     * @param alert State to be added into the data pipeline
     */
    fun register(alert: Mono<State>): Mono<*>
}