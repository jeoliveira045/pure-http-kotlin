package kotlinz.pure.http.controller

import com.sun.net.httpserver.HttpServer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinz.pure.http.model.response.ErrorResponse
import kotlinz.pure.http.repositories.BookRepository
import labs.example.model.Book
import java.time.LocalDate
import java.time.LocalDateTime

class BookController {
    companion object{
        fun createContexts(server: HttpServer){
            val endpoint = "/books"
            server.createContext(endpoint) { exchange ->
                val path = exchange.requestURI.path
                val checkPattern = { string : String ->
                    string.matches(Regex("$endpoint/(\\d+)"))
                }
                when (exchange.requestMethod) {
                    "GET" -> {
                        if(path.matches(Regex("$endpoint/(\\d+)"))){
                            val response = path.replace(Regex("/[a-zA-Z]+/"), "")
                            exchange.sendResponseHeaders(200, response.toByteArray().size.toLong())
                            exchange.responseBody.use { os -> os.write(response.toByteArray()) }
                        } else {
                            val response = Json.encodeToString<List<Book>>(BookRepository.findAll())
                            exchange.sendResponseHeaders(200, response.toByteArray().size.toLong())
                            exchange.responseBody.use { os -> os.write(response.toByteArray()) }
                        }
                    }
                    "POST" -> {
                        try {
                            val response = exchange.requestBody.bufferedReader().use { it.readText() }.trimIndent()
                            val jsonResponse = Json.decodeFromString<Book>(response)
                            BookRepository.insert(jsonResponse)
                            exchange.sendResponseHeaders(202, jsonResponse.toString().toByteArray().size.toLong())
                            exchange.responseBody.use { os -> os.write(jsonResponse.toString().toByteArray())}
                        } catch (e: Exception){
                            val responseError = ErrorResponse(
                                LocalDateTime.now(),
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
                    "PUT" ->  {
                        if(checkPattern(path)) {
                            try {
                                val id = path.replace(Regex("/[a-zA-Z]+/"), "")
                                val requestBody = exchange.requestBody.bufferedReader().use {it.readText()}
                                val bookJson = Json.decodeFromString<Book>(requestBody)
                                val response = Json.encodeToString(BookRepository.update(bookJson))
                                exchange.sendResponseHeaders(200, response.toByteArray().size.toLong())
                                exchange.responseBody.use { os -> os.write(response.toByteArray()) }
                            } catch(e: Exception){
                                e.printStackTrace()
                            }

                        }
                    }
                    "DELETE" -> {
                        if(checkPattern(path)){
                            val id = path.replace(Regex("/[a-zA-Z]+/"), "")
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
