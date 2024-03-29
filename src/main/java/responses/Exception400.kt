package responses

import helpers.StatisticHelper
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

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

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Incorrect date format. Must be in 31.12.2018 format")
    class IncorrectDateFormat : Exception400()

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Incorrect Surname/Name/Lastname format.")
    class IncorrectSNLFormat : Exception400()

}
