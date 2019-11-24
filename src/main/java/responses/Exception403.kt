package responses

import helpers.StatisticHelper
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Forbidden")
open class Exception403 : RuntimeException() {

    init {
        val db = StatisticHelper()
        db.incrementCount("Exception403")
    }

    @ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "You have no permissions to make this request")
    class NoPermissions: Exception403()
}

