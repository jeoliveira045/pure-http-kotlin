package kotlinz.pure.http.repositories

import kotlinz.pure.http.database.AuthorBookRepository
import kotlinz.pure.http.database.AuthorRepository
import kotlinz.pure.http.database.BookRepository

class RepositoryExecutor{
    companion object {
        fun execute(): Unit{
            AuthorRepository.createTable()
            BookRepository.createTable()
            AuthorBookRepository.createTable()
        }
    }
}
