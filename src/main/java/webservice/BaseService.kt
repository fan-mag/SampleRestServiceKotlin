package webservice

import helpers.DatabaseHelper
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

open class BaseService {
    protected val db = DatabaseHelper()
    protected fun validateHeaders(contentType: String) {
        if (contentType != "application/json") throw Exception400()
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Bad request")
    class Exception400 : RuntimeException()

    @ResponseStatus(code = HttpStatus.NO_CONTENT, reason = "No content")
    class Success201 : RuntimeException()

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Unauthorized")
    class Exception401 : RuntimeException()
}