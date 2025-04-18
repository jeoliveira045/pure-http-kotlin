package labs.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Book(
    var id: Long? = null,
    var name: String,
    var isbn: String,
    var author: List<Author> = mutableListOf()
)
