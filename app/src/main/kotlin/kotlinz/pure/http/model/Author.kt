package labs.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Author(
    var id: Long,
    var name: String,
    var age: Int,
    var lastName: String
)
