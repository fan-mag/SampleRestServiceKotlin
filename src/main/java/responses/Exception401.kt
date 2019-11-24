package responses

import helpers.StatisticHelper
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "You are not authorized in the system")
open class Exception401 : RuntimeException() {

    init {
        val db = StatisticHelper()
        db.incrementCount("Exception401")
    }

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "No API key header provided")
    class NoApiKey : Exception401()

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Empty API key header provided")
    class EmptyApiKey : Exception401()

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Invalid API key header provided")
    class InvalidApiKey : Exception401()

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Incorrect type API key header provided")
    class IncorrectTypeApiKey : Exception401()
}