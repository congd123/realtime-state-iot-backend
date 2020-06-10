package com.ddss.data.backend.kafka

import iot.state.State
import iot.trigger.Trigger
import org.apache.avro.Schema
import org.apache.avro.generic.GenericData
import org.apache.avro.generic.GenericRecordBuilder
import java.io.File

interface TypeBuilder {
    fun build(record: Any): Pair<GenericData.Record, Schema>
}

class TypeAvroBuilder : TypeBuilder {
    private var schemaState: Schema = Schema.Parser().parse(File("src/main/avro/state.avsc"))
    private var schemaAlert: Schema = Schema.Parser().parse(File("src/main/avro/trigger.avsc"))

    override fun build(record: Any): Pair<GenericData.Record, Schema> =
        when (record) {
            is State -> {
                Pair(GenericRecordBuilder(schemaState).apply {
                    set("id", record.getId())
                    set("name", record.getName())
                    set("value", record.getValue())
                    set("timestamp", record.getTimestamp())
                    set("device", record.getDevice())
                }.build(), schemaState)
            }
            is Trigger -> {
                Pair(GenericRecordBuilder(schemaAlert).apply {
                    set("id", record.getId())
                    set("name", record.getName())
                    set("state", record.getState())
                    set("timestamp", record.getTimestamp())
                    set("value", record.getValue())
                    set("operator", record.getOperator())
                    set("device", record.getDevice())
                }.build(), schemaAlert)
            }
            else -> throw IllegalArgumentException("Invalid type")
        }
}