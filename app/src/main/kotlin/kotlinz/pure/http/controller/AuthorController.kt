package kotlinz.pure.http.controller

import com.sun.net.httpserver.HttpServer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinz.pure.http.repositories.AuthorRepository
import labs.example.model.Author

class AuthorController {
    companion object{
        @OptIn(ExperimentalSerializationApi::class)
        fun createContexts(server: HttpServer){
            server.createContext("/author") { exchange ->
                val path = exchange.requestURI.path
                val checkPattern = { string : String ->
                    string.matches(Regex("/author/(\\d+)"))
                }
                when (exchange.requestMethod) {
                    "GET" -> {
                        if(path.matches(Regex("/author/(\\d+)"))){
                            val response = path.replace(Regex("/[a-zA-Z]+/"), "")
                            if(!response.matches(Regex("(\\d+)"))){
                                throw Exception("Id inválido")
                            }
                            val author = Json.encodeToString(AuthorRepository.findById(response))
                            exchange.sendResponseHeaders(200, author.toByteArray().size.toLong())
                            exchange.responseBody.use { os -> os.write(author.toByteArray()) }
                        } else {
                            val authorList = Json.encodeToString(AuthorRepository.findAll())
                            exchange.sendResponseHeaders(200, authorList.toByteArray().size.toLong())
                            exchange.responseBody.use { os -> os.write(authorList.toByteArray()) }
                        }
                    }
                    "POST" -> {
                        val response = exchange.requestBody.bufferedReader().use { it.readText() }.trimIndent()
                        val jsonResponse = Json.decodeFromString<Author>(response)
                        AuthorRepository.insert(jsonResponse)
                        exchange.sendResponseHeaders(201, response.toByteArray().size.toLong())
                        exchange.responseBody.use { os -> os.write(response.toByteArray())}
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
