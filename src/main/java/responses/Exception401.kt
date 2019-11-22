package responses

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "You are not authorized in the system")
class Exception401 : RuntimeException() {

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "No API key header provided")
    class NoApiKey : RuntimeException()

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Empty API key header provided")
    class EmptyApiKey : RuntimeException()

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Invalid API key header provided")
    class InvalidApiKey : RuntimeException()

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Incorrect type API key header provided")
    class IncorrectTypeApiKey : RuntimeException()
}