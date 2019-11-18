package webservice

import com.jayway.jsonpath.InvalidJsonException
import com.jayway.jsonpath.JsonPath
import org.springframework.boot.ExitCodeGenerator
import java.util.concurrent.atomic.AtomicLong

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.lang.ClassCastException
import java.lang.RuntimeException
import java.util.*
import javax.servlet.http.HttpServletResponse

@SpringBootApplication
@RestController
open class WebService {
    private val counter = AtomicLong()
    private val db = DatabaseHelper()

    class Passport internal constructor(var id: Long, var person: Person, var serie: Int, var number: Int)
    class Person internal constructor(var id: Long, var surname: String, var name: String, var lastname: String, var birthDate: Date)
    class Greeting internal constructor(val id: Long, val content: String)
    class ApiKey internal constructor(val api_key: String)
    class Result internal constructor(val result: Long)
    @ResponseStatus(code = HttpStatus.I_AM_A_TEAPOT, reason = "Seems to be Class Cast Exception")
    class CustomClassCastException : RuntimeException()

    @ResponseStatus(code = HttpStatus.I_AM_A_TEAPOT, reason = "Seems to be Invalid Json Exception")
    class CustomParseException : RuntimeException()

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Bad Credentials")
    class InvalidCredentialException : RuntimeException()

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Bad Api Key provided / Unauthorized")
    class NoApiKeyProvidedException : RuntimeException()

    @ResponseStatus(code = HttpStatus.NO_CONTENT, reason = "No returned data for this query")
    class NoContentError : RuntimeException()

    @PutMapping("/login")
    fun getApiKey(@RequestBody(required = true) body: String, response: HttpServletResponse): ApiKey {
        val login: String = JsonPath.parse(body).read("$['login']")
        val password: String = JsonPath.parse(body).read("$['password']")
        return ApiKey(db.getApiKey(login, password))
    }

    @RequestMapping("/person", method = arrayOf(RequestMethod.GET))
    fun personGet(@RequestParam(value = "surname", defaultValue = "") surname: String,
                  @RequestParam(value = "name", defaultValue = "") name: String,
                  @RequestParam(value = "lastname", defaultValue = "") lastname: String,
                  @RequestParam(value = "id", defaultValue = "0") id: Int,
                  @RequestHeader(value = "Api-Key", defaultValue = "") apiKey: String) : ArrayList<Person> {
        db.validateApiKey(apiKey)
        if (id != 0)
            return db.getPerson(id)
        else return db.getPerson(surname, name, lastname)
    }


    @RequestMapping("/greeting", method = arrayOf(RequestMethod.GET))
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String,
                 @RequestParam(value = "lastname", defaultValue = "Worldovich") lastname: String): Greeting {
        return Greeting(counter.incrementAndGet(), java.lang.String.format(template, name, lastname))
    }

    @RequestMapping("/calcsumm", method = arrayOf(RequestMethod.GET))
    fun calcsumm(@RequestParam(value = "first", defaultValue = "0") first: Long,
                 @RequestParam(value = "second", defaultValue = "0") second: Long): Result {
        counter.incrementAndGet()
        return Result(first + second)
    }

    @RequestMapping("/calcdiv", method = arrayOf(RequestMethod.GET))
    fun calcdiv(@RequestParam(value = "first", defaultValue = "0") first: Long,
                @RequestParam(value = "second", defaultValue = "1") second: Long): Result {
        counter.incrementAndGet()
        return Result(first / second)
    }

    @RequestMapping("/calcsumm", method = arrayOf(RequestMethod.PUT))
    fun calcsumm(@RequestBody(required = true) body: String): Result {
        try {
            val first: Long = JsonPath.parse(body).read("$['first']")
            val second: Long = JsonPath.parse(body).read("$['second']")
            return Result(first + second)
        } catch (e: ClassCastException) {
            throw CustomClassCastException()
        } catch (e: InvalidJsonException) {
            throw CustomParseException()
        }
    }

    @RequestMapping("/count")
    fun count(): Result {
        return Result(counter.incrementAndGet())
    }

    @RequestMapping("/stop")
    fun stop() {
        db.stop()
        SpringApplication.exit(application, ExitCodeGenerator { -> 0 })
    }

    companion object {
        private val template = "Hello, %s %s!"
        private lateinit var application: ConfigurableApplicationContext

        @JvmStatic
        fun main(args: Array<String>) {
            application = SpringApplication.run(WebService::class.java, *args)
        }
    }
}