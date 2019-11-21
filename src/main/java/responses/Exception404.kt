package responses

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "You have no privileges to make this request")
class Exception404 : RuntimeException() {

    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Person not found for this ID")
    class NoPerson : RuntimeException()
}