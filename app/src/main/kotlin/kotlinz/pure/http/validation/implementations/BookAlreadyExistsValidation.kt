package kotlinz.pure.http.validation.implementations


import kotlinz.pure.http.validation.interfaces.Validators
import labs.example.model.Book
import java.sql.Connection

class BookAlreadyExistsValidation: Validators {
    override fun check(objChecked: Any, connection: Connection) {
        val book = objChecked as Book

        if(book.id != null){

            var sql = """
                SELECT ID, NAME, ISBN FROM BOOK WHERE ID = ${book.id}
            """.trimIndent()

            val stmt = connection.prepareStatement(sql)

            stmt.execute()

            stmt.resultSet.use {
                var selectedBook: Book? = null
                while(it.next()){
                    selectedBook = Book(
                        it.getLong("id"),
                        it.getString("name"),
                        it.getString("isbn")
                    )
                }
                if(book.id == selectedBook!!.id){
                    throw RuntimeException("O livro já existe na base")
                }
            }
        }

    }
}
