package kotlinz.pure.http.validation.implementations

import kotlinz.pure.http.validation.interfaces.Validators
import java.sql.Connection

class BookDataDoNotExists: Validators {
    override fun check(objChecked: Any, connection: Connection) {

    }
}
