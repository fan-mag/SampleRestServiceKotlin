package webservice

import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.PathNotFoundException
import helpers.CredentialsHelper
import helpers.PassportHelper
import helpers.PersonHelper
import helpers.StatisticHelper
import responses.Exception400
import responses.Exception401
import responses.Exception403
import responses.Exception404


open class BaseService {
    protected val dbPerson = PersonHelper()
    protected val dbCredentials = CredentialsHelper()
    protected val dbStatistic = StatisticHelper()
    protected val dbPassport = PassportHelper()

    protected fun validateHeaders(contentType: String) {
        if (contentType != "application/json") throw Exception400()
    }

    protected fun validJsonParse(body: String, key: String): Any {
        try {
            return JsonPath.parse(body).read(key)
        } catch (exception: PathNotFoundException) {
            throw Exception400()
        }
    }

    protected fun jsonParse(body: String, key: String): Any? {
        try {
            return JsonPath.parse(body).read(key)
        } catch (exception: PathNotFoundException) {
            return null
        }
    }

    protected fun validateLongCast(any: Any): Long {
        try {
            if (any is Int)
                return any.toLong()
            return any as Long
        } catch (exception: ClassCastException) {
            throw Exception400()
        }
    }

    fun validatePersonInDatabase(id: Long): Boolean {
        if (!dbPerson.getPerson(id).isEmpty()) return true
        else throw Exception404.NoPerson()
    }

    protected fun validateApiKey(apiKey: String?, privilegeLevel: Int) {
        if (apiKey == null) throw Exception401()
        if (apiKey == "") throw Exception400()
        val code: Int = dbCredentials.validateApiKey(apiKey, privilegeLevel)
        if (code == 401) throw Exception401()
        if (code == 403) throw Exception403()
    }

    protected fun incrementCount() {
        dbStatistic.incrementCount()
    }


}