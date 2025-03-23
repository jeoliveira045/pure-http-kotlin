package kotlinz.pure.http.validation

import kotlinz.pure.http.validation.interfaces.Validators
import java.sql.Connection

class ValidatorExecutor {
    fun exec(validators: List<Validators>, objChecked: Any, conn: Connection){
        validators.forEach {
            it.check(objChecked, conn)
        }
    }
}
