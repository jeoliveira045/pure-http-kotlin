package kotlinz.pure.http.validation.interfaces

import java.sql.Connection

interface Validators {
    fun check(objChecked: Any, connection: Connection)
}
