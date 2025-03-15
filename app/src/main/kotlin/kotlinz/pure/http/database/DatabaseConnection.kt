package kotlinz.pure.http.database

import labs.example.model.Author
import java.sql.Connection
import java.sql.DriverManager

class DatabaseConnection {
    companion object {
        var url = "jdbc:postgresql://localhost:5432/bookmanager"
        var user = "bookmanager"
        var pass = "bookmanager"

        var conn = DriverManager.getConnection(url, user, pass)

        fun getConnection(): Connection{
            return conn;
        }
    }
}








