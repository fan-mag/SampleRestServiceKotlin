package responses

import helpers.StatisticHelper
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Not Found")
open class Exception404 : RuntimeException() {

    init {
        val db = StatisticHelper()
        db.incrementCount("Exception404")
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Person not found for this ID")
    class NoPerson : Exception404()
}