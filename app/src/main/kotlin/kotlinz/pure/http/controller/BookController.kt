package kotlinz.pure.http.controller

import com.sun.net.httpserver.HttpServer
import kotlinx.serialization.json.Json
import labs.example.model.Book

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
                            val response = "Retornou todos os livros"
                            exchange.sendResponseHeaders(200, response.toByteArray().size.toLong())
                            exchange.responseBody.use { os -> os.write(response.toByteArray()) }
                        }
                    }
                    "POST" -> {
                        try {
                            val response = exchange.requestBody.bufferedReader().use { it.readText() }.trimIndent()
                            val jsonResponse = Json.decodeFromString<Book>(response)
                            exchange.sendResponseHeaders(202, jsonResponse.toString().toByteArray().size.toLong())
                            exchange.responseBody.use { os -> os.write(jsonResponse.toString().toByteArray())}
                        } catch (e: Exception){
                            e.printStackTrace()
                        }

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
