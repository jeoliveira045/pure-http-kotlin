package kotlinz.pure.http.validation.implementations

import kotlinz.pure.http.validation.interfaces.Validators
import labs.example.model.Author
import java.sql.Connection

class AuthorAlreadyExistsValidation: Validators {
    override fun check(objChecked: Any, conn: Connection) {
        val author = objChecked as Author

        val sql = """
            SELECT ID, NAME, AGE, LASTNAME FROM AUTHOR WHERE ID = ${author.id}
        """.trimIndent()

        val stmt = conn.prepareStatement(sql)

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

        if(author.id == selectedAuthor!!.id){
            throw Exception("Autor não encotrado!")
        }
    }
}
