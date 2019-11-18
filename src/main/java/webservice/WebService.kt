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
import java.text.SimpleDateFormat
import java.util.*

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
    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Seems to be Class Cast Exception")
    class CustomClassCastException : RuntimeException()

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Seems to be Invalid Json Exception")
    class CustomParseException : RuntimeException()

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Bad Credentials")
    class InvalidCredentialException : RuntimeException()

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Bad Api Key provided / Unauthorized")
    class NoApiKeyProvidedException : RuntimeException()

    @ResponseStatus(code = HttpStatus.NO_CONTENT, reason = "No returned data for this query")
    class NoContentException : RuntimeException()

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid Content-Type Header")
    class InvalidContentTypeException : RuntimeException()

    @PutMapping("/login")
    fun getApiKey(@RequestBody(required = true) body: String,
                  @RequestHeader(value = "Content-Type") contentType: String): ApiKey {
        db.incrementCount()
        validateHeaders(contentType)
        val login: String = JsonPath.parse(body).read("$['login']")
        val password: String = JsonPath.parse(body).read("$['password']")
        return ApiKey(db.getApiKey(login, password))
    }

    @GetMapping("/person")
    fun personGet(@RequestParam(value = "surname", defaultValue = "") surname: String,
                  @RequestParam(value = "name", defaultValue = "") name: String,
                  @RequestParam(value = "lastname", defaultValue = "") lastname: String,
                  @RequestParam(value = "id", defaultValue = "0") id: Int,
                  @RequestHeader(value = "Api-Key", defaultValue = "") apiKey: String): ArrayList<Person> {
        db.incrementCount()
        db.validateApiKey(apiKey)
        if (id != 0)
            return db.getPerson(id)
        else return db.getPerson(surname, name, lastname)
    }

    @DeleteMapping("/person")
    fun personDelete(@RequestHeader(value = "Api-Key", defaultValue = "") apiKey: String,
                  @RequestHeader(value = "Content-Type", defaultValue = "") contentType: String,
                  @RequestBody(required = true) body: String) {
        db.incrementCount()
        db.validateApiKey(apiKey)
        val id : Long = JsonPath.parse(body).read("$['id']")
        db.deletePerson(id)
    }

    @PutMapping("/person")
    fun personPut(@RequestHeader(value = "Api-Key", defaultValue = "") apiKey: String,
                  @RequestHeader(value = "Content-Type", defaultValue = "") contentType: String,
                  @RequestBody(required = true) body: String): Person {
        db.incrementCount()
        db.validateApiKey(apiKey)
        validateHeaders(contentType)
        val id : Long = JsonPath.parse(body).read("$['id']")
        val surname: String = JsonPath.parse(body).read("$['surname']")
        val name: String = JsonPath.parse(body).read("$['name']")
        val lastname: String = JsonPath.parse(body).read("$['lastname']")
        val birthDate: Date = SimpleDateFormat("yyyy-MM-dd").parse(JsonPath.parse(body).read("$['birthdate']"))
        db.updatePerson(id, surname, name, lastname, birthDate)
        return Person(id, surname, name, lastname, birthDate)
    }

    @PostMapping("/person")
    fun personPost(@RequestHeader(value = "Api-Key", defaultValue = "") apiKey: String,
                   @RequestHeader(value = "Content-Type", defaultValue = "") contentType: String,
                   @RequestBody(required = true) body: String): Person {
        db.incrementCount()
        db.validateApiKey(apiKey)
        validateHeaders(contentType)
        val surname: String = JsonPath.parse(body).read("$['surname']")
        val name: String = JsonPath.parse(body).read("$['name']")
        val lastname: String = JsonPath.parse(body).read("$['lastname']")
        val birthDate: Date = SimpleDateFormat("yyyy-MM-dd").parse(JsonPath.parse(body).read("$['birthdate']"))
        val id: Long = db.createPerson(surname, name, lastname, birthDate)
        return Person(id, surname, name, lastname, birthDate)
    }


    @RequestMapping("/greeting", method = arrayOf(RequestMethod.GET))
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String,
                 @RequestParam(value = "lastname", defaultValue = "Worldovich") lastname: String): Greeting {
        db.incrementCount()
        return Greeting(counter.incrementAndGet(), java.lang.String.format("Hello, %s, %s", name, lastname))
    }

    @RequestMapping("/calcsumm", method = arrayOf(RequestMethod.GET))
    fun calcsumm(@RequestParam(value = "first", defaultValue = "0") first: Long,
                 @RequestParam(value = "second", defaultValue = "0") second: Long): Result {
        db.incrementCount()
        return Result(first + second)
    }

    @RequestMapping("/calcdiv", method = arrayOf(RequestMethod.GET))
    fun calcdiv(@RequestParam(value = "first", defaultValue = "0") first: Long,
                @RequestParam(value = "second", defaultValue = "1") second: Long): Result {
        db.incrementCount()
        return Result(first / second)
    }

    @RequestMapping("/calcsumm", method = arrayOf(RequestMethod.PUT))
    fun calcsumm(@RequestBody(required = true) body: String): Result {
        db.incrementCount()
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
        db.incrementCount()
        return Result(db.getCount())
    }

    private fun validateHeaders(contentType: String) {
        if (contentType != "application/json") throw InvalidContentTypeException()
    }

    companion object {
        private lateinit var application: ConfigurableApplicationContext

        @JvmStatic
        fun start() {
        }

        @JvmStatic
        fun stop() {
            SpringApplication.exit(application, ExitCodeGenerator { -> 0 })
        }

        @JvmStatic
        fun main(args: Array<String>) {
            application = SpringApplication.run(WebService::class.java)
        }
    }


}
