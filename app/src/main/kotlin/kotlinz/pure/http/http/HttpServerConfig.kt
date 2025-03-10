package kotlinz.pure.http.http

import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress

class HttpServerConfig {
    companion object {
        var server: HttpServer = HttpServer.create(InetSocketAddress(8080), 0)

        fun createHttpServer(port: Int) = run {
            server.executor = null
            server.start()
        }
    }

}
