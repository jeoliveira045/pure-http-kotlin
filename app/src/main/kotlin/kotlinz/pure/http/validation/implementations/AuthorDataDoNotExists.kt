package kotlinz.pure.http.validation.implementations

import kotlinz.pure.http.validation.interfaces.Validators
import labs.example.model.Author
import java.sql.Connection

class AuthorDataDoNotExists: Validators {
    override fun check(objChecked: Any, connection: Connection){
        val author = objChecked as Author

        val sql = """
            SELECT ID, NAME, AGE, LASTNAME FROM AUTHOR WHERE ID = ${author.id}
        """.trimIndent()

        val stmt = connection.prepareStatement(sql)

        stmt.execute()

        var selectedAuthor: Author? = null

        stmt.resultSet.use {
            while(it.next()){
                selectedAuthor = Author(
                    it.getLong("id"),
                    it.getString("name"),
                    it.getInt("age"),
                    it.getString("lastname")
                )
            }
        }

        if(selectedAuthor == null){
            throw RuntimeException("Autor não encotrado!")
        }
    }
}
