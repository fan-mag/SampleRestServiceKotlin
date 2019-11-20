package webservice

import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.PathNotFoundException
import helpers.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

open class BaseService {
    protected val dbPerson = PersonHelper()
    protected val dbCredentials = CredentialsHelper()
    protected val dbStatistic = StatisticHelper()
    protected val dbPassport = PassportHelper()

    protected fun validateHeaders(contentType: String) {
        if (contentType != "application/json") throw Exception400()
    }

    protected fun validateApiKey(apiKey: String, privilegeLevel: Int) {
        dbCredentials.validateApiKey(apiKey, privilegeLevel)
    }

    protected fun incrementCount() {
        dbStatistic.incrementCount()
    }

    protected fun jsonParse(body: String, key: String): Any? {
        try {
            return JsonPath.parse(body).read(key)
        } catch (exception : PathNotFoundException) {
            return null
        }
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Bad request")
    class Exception400 : RuntimeException()

    @ResponseStatus(code = HttpStatus.NO_CONTENT, reason = "No content")
    class Success201 : RuntimeException()

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Unauthorized")
    class Exception401 : RuntimeException()

    @ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Forbidden")
    class Exception403 : RuntimeException()
}