package kotlinz.pure.http.repositories

import kotlinz.pure.http.database.DatabaseConnection

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
