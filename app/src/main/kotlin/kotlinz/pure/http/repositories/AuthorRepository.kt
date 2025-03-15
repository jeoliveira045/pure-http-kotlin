package kotlinz.pure.http.repositories

import kotlinz.pure.http.database.DatabaseConnection
import labs.example.model.Author

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

        fun findAll(): List<Author>{
            val stmt = connection.createStatement()
            val sql = """
                SELECT ID, NAME, AGE, LASTNAME FROM AUTHOR;
            """.trimIndent()

            stmt.execute(sql)

            val authorList = mutableListOf<Author>()

            stmt.resultSet.use { rs ->
                while(rs.next()){
                    val author = Author(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("lastName")
                    )
                    authorList.add(author)
                }
            }
            return authorList
        }

        fun findById(id: Any): Author {
            val sql = """
                SELECT ID, NAME, AGE, LASTNAME FROM AUTHOR WHERE ID = ?;
            """.trimIndent()
            val stmt = connection.prepareStatement(sql)
            stmt.setInt(1, id as Int)

            stmt.execute()

            val authorList = mutableListOf<Author>()

            stmt.resultSet.use { rs ->
                while(rs.next()){
                    val author = Author(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("lastName")
                    )
                    authorList.add(author)
                }
            }
            return authorList.findLast { it.id == id as Long }!!
        }

        fun update(author: Author): Author {
            val sql = """
                UPDATE AUTHOR SET ID = ?, NAME = ?, AGE = ?, LASTNAME = ? WHERE ID = ? 
            """.trimIndent()
            val stmt = connection.prepareStatement(sql)
            stmt.setInt(1, author.id as Int)
            stmt.setString(2, author.name)
            stmt.setInt(3, author.age)
            stmt.setString(4, author.lastName)
            stmt.setInt(5, author.id as Int)
            stmt.execute()

            val authorList = mutableListOf<Author>()

            stmt.resultSet.use { rs ->
                while(rs.next()){
                    val author = Author(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("lastName")
                    )
                    authorList.add(author)
                }
            }
            return authorList.findLast { it.id == author.id }!!
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
