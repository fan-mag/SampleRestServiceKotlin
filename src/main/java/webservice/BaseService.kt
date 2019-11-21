package webservice

import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.PathNotFoundException
import errors.Exception400
import helpers.CredentialsHelper
import helpers.PassportHelper
import helpers.PersonHelper
import helpers.StatisticHelper


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

}