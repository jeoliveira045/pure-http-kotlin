package kotlinz.pure.http.repositories

import kotlinz.pure.http.database.DatabaseConnection


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
