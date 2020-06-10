package com.ddss.data.backend.service

import com.google.gson.Gson
import com.ddss.data.backend.kafka.KafkaProducer
import iot.state.State
import org.apache.flink.api.common.ExecutionConfig
import org.apache.flink.api.common.JobID
import org.apache.flink.api.common.state.MapState
import org.apache.flink.api.common.state.MapStateDescriptor
import org.apache.flink.api.common.typeinfo.BasicTypeInfo
import org.apache.flink.api.common.typeinfo.Types
import org.apache.flink.queryablestate.client.QueryableStateClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import java.util.concurrent.CompletableFuture


@Component
class FlinkStateService: StateService {
    @Autowired
    private lateinit var producer: KafkaProducer<State>

    private lateinit var client: QueryableStateClient

    override fun init(host: String, port: String): Mono<Boolean?> {
        client = QueryableStateClient(host, port.toInt())

        return true.toMono()
    }

    override fun getState(id: String, jobId: String): Mono<List<State>?> {
        val states: MapState<String, String> = queryState(id, JobID.fromHexString(jobId), client).get()

        return states.iterator().asSequence().map {
            entry -> Gson().fromJson(entry.value, State::class.java)
        }.toList().toMono()
    }

    override fun register(alert: Mono<State>): Mono<*> {
        return alert.subscribe {
            producer.send("devices.state", it.getId().toString(), it)
            true
        }.toMono()
    }

    fun queryState(key: String, jobId:JobID, client: QueryableStateClient): CompletableFuture<MapState<String, String>> {
        val descriptor: MapStateDescriptor<String, String> = MapStateDescriptor(
            "windowDevicesState",
            BasicTypeInfo.STRING_TYPE_INFO,
            BasicTypeInfo.STRING_TYPE_INFO
        )
        val executionConfig = ExecutionConfig()
        executionConfig.enableForceKryo()
        client.setExecutionConfig(executionConfig)

        return client.getKvState(jobId, "devices-state", key, Types.STRING, descriptor)
    }
}