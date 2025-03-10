package kotlinz.pure.http.controller

import com.sun.net.httpserver.HttpServer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import labs.example.model.Author

class AuthorController {
    companion object{
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
                            exchange.sendResponseHeaders(200, response.toByteArray().size.toLong())
                            exchange.responseBody.use { os -> os.write(response.toByteArray()) }
                        } else {
                            val response = "Retornou todos os autores"
                            exchange.sendResponseHeaders(200, response.toByteArray().size.toLong())
                            exchange.responseBody.use { os -> os.write(response.toByteArray()) }
                        }
                    }
                    "POST" -> {
                        val response = exchange.requestBody.bufferedReader().use { it.readText() }.trimIndent()
                        val jsonResponse = Json.decodeFromString<Author>(response)
                        exchange.sendResponseHeaders(201, response.toByteArray().size.toLong())
                        exchange.responseBody.use { os -> os.write(response.toByteArray())}
                    }
                    "PUT" ->  {
                        if(checkPattern(path)) {
                            val id = path.replace(Regex("/[a-zA-Z]+/"), "")
                            val requestBody = exchange.requestBody.bufferedReader().use {it.readText()}
                            println(requestBody)
                            val response = "O seguinte id foi atualizado: $id"
                            exchange.sendResponseHeaders(200, response.toByteArray().size.toLong())
                            exchange.responseBody.use { os -> os.write(response.toByteArray()) }
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
