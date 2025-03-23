package kotlinz.pure.http.model.response

import kotlinx.serialization.Serializable
import kotlinz.pure.http.model.databind.LocalDateDatabind
import java.time.LocalDate

@Serializable
data class ErrorResponse (
    @Serializable(with = LocalDateDatabind::class)
    var timestamp: LocalDate? = null,
    var code: Int? = null,
    var error: String? = null,
    var message: String? = null,
    var path: String? = null
)
