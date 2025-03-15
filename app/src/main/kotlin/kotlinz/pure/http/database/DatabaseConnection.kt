package kotlinz.pure.http.database

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


class AuthorRepository{
    companion object {
        private var connection = DatabaseConnection.getConnection()

        fun createTable(){
            val stmt = connection.createStatement()
            val sql = """
            CREATE TABLE IF NOT EXISTS author (
                id SERIAL PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                age INT NOT NULL,
                lastName VARCHAR(100) NOT NULL
            )
        """;
            stmt.executeUpdate(sql)
        }

    }
}

class BookRepository{
    companion object{
        private var connection = DatabaseConnection.getConnection()

        fun createTable() {
            var stmt = connection.createStatement()
            val sql = """
            CREATE TABLE IF NOT EXISTS book (
                id SERIAL PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                isbn VARCHAR(100) NOT NULL
            )
        """;

            stmt.executeUpdate(sql);
        }
    }
}

class AuthorBookRepository{
    companion object {
        private var connection = DatabaseConnection.getConnection()

        fun createTable() {
            var stmt = connection.createStatement()
            val sql = """
            CREATE TABLE IF NOT EXISTS author_book (
                AUTHOR_ID INTEGER REFERENCES AUTHOR(ID),
                BOOK_ID INTEGER REFERENCES BOOK(ID),
                PRIMARY KEY (AUTHOR_ID, BOOK_ID)
            )
        """;

            stmt.executeUpdate(sql);
        }
    }
}


