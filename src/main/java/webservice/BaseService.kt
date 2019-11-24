package webservice

import com.jayway.jsonpath.InvalidJsonException
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
import java.text.ParseException
import java.util.*


open class BaseService {
    protected val dbPerson = PersonHelper()
    protected val dbCredentials = CredentialsHelper()
    protected val dbStatistic = StatisticHelper()
    protected val dbPassport = PassportHelper()
    protected val startDate = Date(Date().time + 10_800_000L)

    protected fun validateHeaders(contentType: String) {

        if (!contentType.contains("application/json")) throw Exception400.NoContentType()
    }

    protected fun validJsonParse(body: String, key: String): Any {
        try {
            return JsonPath.parse(body).read(key)
        } catch (exception: Exception) {
            when (exception) {
                is PathNotFoundException -> throw Exception400.NoData()
                is InvalidJsonException, is ParseException -> throw Exception400.IncorrectJson()
                else -> throw exception
            }
        }
    }

    protected fun jsonParse(body: String, key: String): Any? {
        try {
            return JsonPath.parse(body).read(key)
        } catch (exception: Exception) {
            when (exception) {
                is PathNotFoundException -> return null
                is InvalidJsonException, is ParseException -> throw Exception400.IncorrectJson()
                else -> throw exception
            }
        }
    }

    protected fun parseStringFromJson(body: String, key: String): String? {
        try {
            val any: Any? = jsonParse(body, key) ?: return null
            return any.toString()
        } catch (exception: ClassCastException) {
            throw Exception400.ClassCast()
        }
    }

    protected fun parseIntFromJson(body: String, key: String): Int? {
        try {
            val any: Any? = jsonParse(body, key) ?: return null
            return any.toString().toInt()
        } catch (exception: ClassCastException) {
            throw Exception400.ClassCast()
        }
    }

    protected fun parseLongFromJson(body: String, key: String): Long? {
        try {
            val any: Any? = jsonParse(body, key) ?: return null
            if (any is Int)
                return any.toLong()
            return any as Long
        } catch (exception: ClassCastException) {
            throw Exception400.ClassCast()
        }
    }

    protected fun parseValidStringFromJson(body: String, key: String): String {
        try {
            return validJsonParse(body, key).toString()
        } catch (exception: ClassCastException) {
            throw Exception400.ClassCast()
        }
    }


    protected fun parseValidLongFromJson(body: String, key: String): Long {
        try {
            val any: Any = validJsonParse(body, key)
            if (any is Int)
                return any.toLong()
            return any as Long
        } catch (exception: ClassCastException) {
            throw Exception400.ClassCast()
        }
    }

    protected fun validateApiKey(apiKey: String?, privilegeLevel: Int) {
        if (apiKey == null) throw Exception401.NoApiKey()
        if (apiKey == "") throw Exception401.EmptyApiKey()
        if (!apiKey.matches(Regex("^\\d+$"))) throw Exception401.IncorrectTypeApiKey()
        val code: Int = dbCredentials.validateApiKey(apiKey, privilegeLevel)
        if (code == 401) throw Exception401.InvalidApiKey()
        if (code == 403) throw Exception403.NoPermissions()
    }

    protected fun incrementCount() {
        dbStatistic.incrementCount("TotalRequests")
    }

    protected fun validateParamAsInt(input: String?): Int? {
        try {
            return input?.toInt()
        } catch (exception: NumberFormatException) {
            throw Exception400.ClassCast()
        }
    }

    protected fun validatePassport(passport: String?) {
        if (passport != null) {
            try {
                val split = passport.split("-")
                if (split.size != 2) throw Exception400.InvalidPassportType()
                Integer.parseInt(split[0])
                Integer.parseInt(split[1])
            } catch (exception: Exception) {
                when (exception) {
                    is IndexOutOfBoundsException -> throw Exception400.InvalidPassportType()
                    is NumberFormatException -> throw Exception400.InvalidPassportType()
                    else -> throw exception
                }
            }
        }
    }


}