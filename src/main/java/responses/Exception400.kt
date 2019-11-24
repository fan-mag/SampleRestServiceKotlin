package responses

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import helpers.StatisticHelper

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Bad Request")
open class Exception400 : RuntimeException() {

    init {
        val db = StatisticHelper()
        db.incrementCount("Exception400")
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Incorrect data type")
    class ClassCast : Exception400()

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Required data was not provided")
    class NoData : Exception400()

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Incorrect format (not a json)")
    class IncorrectJson : Exception400()

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Incorrect Content-Type Header")
    class NoContentType : Exception400()

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Passport format must be in 0000-000000 format")
    class InvalidPassportType : Exception400()

}
