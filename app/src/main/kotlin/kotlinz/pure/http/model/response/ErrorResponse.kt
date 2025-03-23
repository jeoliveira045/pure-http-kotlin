package kotlinz.pure.http.model.response

import kotlinx.serialization.Serializable
import kotlinz.pure.http.model.databind.LocalDateTimeDatabind
import java.time.LocalDateTime

@Serializable
data class ErrorResponse (
    @Serializable(with = LocalDateTimeDatabind::class)
    var timestamp: LocalDateTime? = null,
    var code: Int? = null,
    var error: String? = null,
    var message: String? = null,
    var path: String? = null
)
