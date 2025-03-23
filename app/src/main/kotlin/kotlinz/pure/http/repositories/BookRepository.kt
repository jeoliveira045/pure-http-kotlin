package kotlinz.pure.http.repositories

import kotlinz.pure.http.database.DatabaseConnection
import kotlinz.pure.http.validation.ValidatorExecutor
import kotlinz.pure.http.validation.implementations.AuthorAlreadyExistsValidation
import kotlinz.pure.http.validation.implementations.AuthorDataDoNotExists
import kotlinz.pure.http.validation.implementations.BookAlreadyExistsValidation
import kotlinz.pure.http.validation.implementations.BookDataDoNotExists
import kotlinz.pure.http.validation.interfaces.Validators
import labs.example.model.Author
import labs.example.model.Book


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
        fun findAll(): List<Book>{
            val stmt = connection.createStatement()
            val sql = """
                SELECT B.ID as BOOKID, B.NAME as BOOKNAME, B.ISBN, A.ID AS AUTHORID, A.NAME AS AUTHORNAME, A.AGE, A.lastname 
                FROM BOOK B 
                INNER JOIN AUTHOR_BOOK ON B.ID = AUTHOR_BOOK.BOOK_ID
                INNER JOIN AUTHOR A ON A.ID = AUTHOR_BOOK.AUTHOR_ID
            """.trimIndent()

            stmt.execute(sql)

            val bookList = mutableListOf<Book>()

            stmt.resultSet.use { rs ->
                while(rs.next()){
                    val author = Book(
                        rs.getLong("bookid"),
                        rs.getString("bookname"),
                        rs.getString("isbn"),
                        listOf(Author(
                            rs.getLong("authorid"),
                            rs.getString("authorname"),
                            rs.getInt("age"),
                            rs.getString("lastName")
                        ))
                    )
                    bookList.add(author)
                }
            }
            val bookGroupBy = bookList.groupBy { it.id }

            bookGroupBy.forEach {(key, value) ->
                val bookAuthorReduced = value.reduce {acc, book ->
                    Book(
                        acc.id,
                        acc.name,
                        acc.isbn,
                        (acc.author + book.author).distinct()
                    )
                }
                bookList.removeAll(value)
                bookList.add(bookAuthorReduced)
            }

            return bookList
        }

        fun findById(id: Any): Book {
            val sql = """
                SELECT B.ID as BOOKID, B.NAME as BOOKNAME, B.ISBN, A.ID AS AUTHORID, A.NAME AS AUTHORNAME, A.AGE, A.lastname 
                FROM BOOK B 
                INNER JOIN AUTHOR_BOOK ON B.ID = AUTHOR_BOOK.BOOK_ID
                INNER JOIN AUTHOR A ON A.ID = AUTHOR_BOOK.AUTHOR_ID
                WHERE B.ID = ?
            """.trimIndent()
            val stmt = connection.prepareStatement(sql)
            stmt.setLong(1, (id as String).toLong())
            stmt.execute()

            var book: Book? = null

            stmt.resultSet.use { rs ->
                val bookList = mutableListOf<Book>()
                while(rs.next()){
                    val author = Book(
                        rs.getLong("bookid"),
                        rs.getString("bookname"),
                        rs.getString("isbn"),
                        listOf(Author(
                            rs.getLong("authorid"),
                            rs.getString("authorname"),
                            rs.getInt("age"),
                            rs.getString("lastName")
                        ))
                    )
                    bookList.add(author)
                }
                val bookGroupBy = bookList.groupBy { it.id }

                bookGroupBy.forEach {(key, value) ->
                    val bookAuthorReduced = value.reduce {acc, book ->
                        Book(
                            acc.id,
                            acc.name,
                            acc.isbn,
                            (acc.author + book.author).distinct()
                        )
                    }
                    book = bookAuthorReduced
                }
            }
            return book!!
        }

        fun insert(book: Book): Book {
            val bookvalidators = listOf<Validators>(BookAlreadyExistsValidation())
            val authorValidators = listOf<Validators>(AuthorDataDoNotExists())
            ValidatorExecutor().exec(bookvalidators, book, connection)
            book.author.forEach {
                ValidatorExecutor().exec(authorValidators, it, connection)
            }

            val sql = """
                INSERT INTO BOOK(NAME, ISBN) VALUES (?, ?)
            """.trimIndent()
            val stmt = connection.prepareStatement(sql)

            stmt.setString(1, book.name)
            stmt.setString(2, book.isbn)
            stmt.execute()

            return book
        }

        fun update(author: Author): Author {
            val sql = """
                UPDATE AUTHOR SET ID = ?, NAME = ?, AGE = ?, LASTNAME = ? WHERE ID = ? 
            """.trimIndent()
            val stmt = connection.prepareStatement(sql)
            stmt.setLong(1, author.id!!)
            stmt.setString(2, author.name)
            stmt.setInt(3, author.age)
            stmt.setString(4, author.lastName)
            stmt.setLong(5, author.id!!)
            stmt.execute()

            return author
        }

        fun delete(id: Int){
            val sql = """
                DELETE FROM AUTHOR WHERE ID = ?
            """.trimIndent()
            val stmt = connection.prepareStatement(sql)
            stmt.setInt(1, id)

            stmt.execute()
        }
    }
}
