package responses

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Bad Request")
class Exception400 : RuntimeException() {

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Incorrect data type")
    class ClassCast : RuntimeException()

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Required data was not provided")
    class NoData : RuntimeException()

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Incorrect format (not a json)")
    class IncorrectJson : RuntimeException()

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Incorrect Content-Type Header")
    class NoContentType : RuntimeException()

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Passport format must be in 0000-000000 format")
    class InvalidPassportType : RuntimeException()
}
