package com.ddss.data.backend.kafka

import org.apache.avro.generic.GenericDatumWriter
import org.apache.avro.generic.GenericRecord
import org.apache.avro.io.BinaryEncoder
import org.apache.avro.io.EncoderFactory
import org.apache.kafka.common.serialization.Serializer
import java.io.ByteArrayOutputStream

class KafkaSerialization<T> : Serializer<T> {
    private var typeAvroBuilder: TypeAvroBuilder =
        TypeAvroBuilder()

    override fun serialize(topic: String?, data: T?): ByteArray {
        if (data == null)
            throw RuntimeException("error")

        val (genericRecord, schema) = typeAvroBuilder.build(data)

        val writer = GenericDatumWriter<GenericRecord>(schema)
        val out = ByteArrayOutputStream()
        val encoder: BinaryEncoder = EncoderFactory.get().binaryEncoder(out, null)
        writer.write(genericRecord, encoder)
        encoder.flush()
        out.close()

        return out.toByteArray()
    }

    override fun configure(p0: MutableMap<String, *>?, p1: Boolean) {}

    override fun close() {}
}