package responses

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Unknown Server Error")
class Exception500 : RuntimeException() {

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Person.GET case no impl")
    class PersonGetNoImpl : RuntimeException()

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "PersonHelper.getQueryBuilder case no impl")
    class PersonDatabaseNoImpl : RuntimeException()
}