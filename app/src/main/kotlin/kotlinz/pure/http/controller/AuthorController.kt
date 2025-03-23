package kotlinz.pure.http.controller

import com.sun.net.httpserver.HttpServer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinz.pure.http.model.response.ErrorResponse
import kotlinz.pure.http.repositories.AuthorRepository
import labs.example.model.Author
import java.time.LocalDate

class AuthorController {
    companion object{
        @OptIn(ExperimentalSerializationApi::class)
        fun createContexts(server: HttpServer){
            val endpoint = "/author"
            server.createContext(endpoint) { exchange ->
                val path = exchange.requestURI.path
                val checkPattern = { string : String ->
                    string.matches(Regex("$endpoint/(\\d+)"))
                }
                when (exchange.requestMethod) {
                    "GET" -> {
                        if(path.matches(Regex("$endpoint/(\\d+)"))){
                            try{
                                val response = path.replace(Regex("/[a-zA-Z]+/"), "")
                                if(!response.matches(Regex("(\\d+)"))){
                                    throw Exception("Id inválido")
                                }
                                val author = Json.encodeToString(AuthorRepository.findById(response))
                                exchange.sendResponseHeaders(200, author.toByteArray().size.toLong())
                                exchange.responseBody.use { os -> os.write(author.toByteArray()) }
                            } catch(e: Exception){
                                val responseError = ErrorResponse(
                                    LocalDate.now(),
                                    500,
                                    "Internal Server Error",
                                    e.message,
                                    endpoint
                                )
                                val jsonResponseError = Json.encodeToString(responseError)
                                exchange.sendResponseHeaders(500, jsonResponseError.toByteArray().size.toLong())
                                exchange.responseBody.use { os -> os.write(jsonResponseError.toByteArray()) }
                                e.printStackTrace()
                            }

                        } else {
                            try {
                                val authorList = Json.encodeToString(AuthorRepository.findAll())
                                exchange.sendResponseHeaders(200, authorList.toByteArray().size.toLong())
                                exchange.responseBody.use { os -> os.write(authorList.toByteArray()) }
                            } catch(e: Exception){
                                val responseError = ErrorResponse(
                                    LocalDate.now(),
                                    500,
                                    "Internal Server Error",
                                    e.message,
                                    endpoint
                                )
                                val jsonResponseError = Json.encodeToString(responseError)
                                exchange.sendResponseHeaders(500, jsonResponseError.toByteArray().size.toLong())
                                exchange.responseBody.use { os -> os.write(jsonResponseError.toByteArray()) }
                                e.printStackTrace()
                            }
                        }
                    }
                    "POST" -> {
                        try {
                            val response = exchange.requestBody.bufferedReader().use { it.readText() }.trimIndent()
                            val jsonResponse = Json.decodeFromString<Author>(response)
                            AuthorRepository.insert(jsonResponse)
                            exchange.sendResponseHeaders(201, response.toByteArray().size.toLong())
                            exchange.responseBody.use { os -> os.write(response.toByteArray())}
                        } catch(e: Exception){
                            val responseError = ErrorResponse(
                                LocalDate.now(),
                                500,
                                "Internal Server Error",
                                e.message,
                                endpoint
                            )
                            val jsonResponseError = Json.encodeToString(responseError)
                            exchange.sendResponseHeaders(500, jsonResponseError.toByteArray().size.toLong())
                            exchange.responseBody.use { os -> os.write(jsonResponseError.toByteArray()) }
                        }

                    }
                    "PUT" ->  {
                        if(checkPattern(path)) {
                            val id = path.replace(Regex("/[a-zA-Z]+/"), "")
                            val requestBody = exchange.requestBody.bufferedReader().use {it.readText()}
                            val author = Json.decodeFromString<Author>(requestBody)
                            AuthorRepository.update(author)
                            val response = "O seguinte id foi atualizado: $id"
                            exchange.sendResponseHeaders(200, response.toByteArray().size.toLong())
                            exchange.responseBody.use { os -> os.write(response.toByteArray()) }
                        }
                    }
                    "DELETE" -> {
                        if(checkPattern(path)){
                            val id = path.replace(Regex("/[a-zA-Z]+/"), "")
                            AuthorRepository.delete(id.toInt())
                            val response = "O seguinte id foi deletado: $id"
                            exchange.sendResponseHeaders(200, response.toByteArray().size.toLong())
                            exchange.responseBody.use { os -> os.write(response.toByteArray()) }
                        }
                    }
                }

            }
        }
    }

}
