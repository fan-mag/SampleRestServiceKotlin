package responses

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Forbidden")
class Exception403 : RuntimeException() {

    @ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "You have no permissions to make this request")
    class NoPermissions: RuntimeException()
}

