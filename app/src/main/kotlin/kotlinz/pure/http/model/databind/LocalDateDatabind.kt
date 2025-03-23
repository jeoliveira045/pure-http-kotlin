package kotlinz.pure.http.model.databind

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object LocalDateDatabind: KSerializer<LocalDate> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    override val descriptor: SerialDescriptor
        get() {
            return PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)
        }

    override fun deserialize(decoder: Decoder): LocalDate {
        val string = decoder.decodeString()
        return LocalDate.parse(string, formatter)
    }

    override fun serialize(encoder: Encoder, value: LocalDate) {
        val string = value.format(formatter)
        return encoder.encodeString(string)
    }

}
