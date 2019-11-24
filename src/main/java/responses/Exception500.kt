package responses

import helpers.StatisticHelper
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Unknown Server Error")
open class Exception500 : RuntimeException() {

    init {
        val db = StatisticHelper()
        db.incrementCount("Exception500")
    }

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Person.GET case no impl")
    class PersonGetNoImpl : Exception500()

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "PersonHelper.getQueryBuilder case no impl")
    class PersonDatabaseNoImpl : Exception500()

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Person.DELETE case no impl")
    class PersonDeleteNoImpl : Exception500() {

    }
}