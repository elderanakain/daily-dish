package io.krugosvet.dailydish.common.core

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind.STRING
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json

internal val jsonConfig: Json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    encodeDefaults = false
}

internal class LocalDateSerializer :
    KSerializer<LocalDate> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDate", STRING)

    override fun deserialize(decoder: Decoder): LocalDate =
        decoder.decodeString().toLocalDate()

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(value.toString())
    }
}
