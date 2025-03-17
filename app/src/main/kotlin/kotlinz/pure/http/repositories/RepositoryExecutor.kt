package kotlinz.pure.http.repositories

import kotlinz.pure.http.repositories.AuthorBookRepository
import kotlinz.pure.http.repositories.AuthorRepository
import kotlinz.pure.http.repositories.BookRepository

class RepositoryExecutor{
    companion object {
        fun execute(): Unit{
            AuthorRepository.createTable()
            BookRepository.createTable()
            AuthorBookRepository.createTable()
        }
    }
}
