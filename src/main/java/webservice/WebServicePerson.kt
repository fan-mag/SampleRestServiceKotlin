package webservice

import com.jayway.jsonpath.JsonPath
import model.Person
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.text.SimpleDateFormat
import java.util.*

@RestController
class WebServicePerson : BaseService() {
    @GetMapping("/person")
    fun personGet(@RequestParam(value = "surname", defaultValue = "") surname: String,
                  @RequestParam(value = "name", defaultValue = "") name: String,
                  @RequestParam(value = "lastname", defaultValue = "") lastname: String,
                  @RequestParam(value = "id", defaultValue = "0") id: Int,
                  @RequestHeader(value = "Api-Key", defaultValue = "") apiKey: String): ResponseEntity<Any> {
        incrementCount()
        validateApiKey(apiKey)
        val persons: ArrayList<Person> = if (id != 0) dbPerson.getPerson(id)
        else dbPerson.getPerson(surname, name, lastname)
        val status: HttpStatus = if (persons.isEmpty()) HttpStatus.NO_CONTENT else HttpStatus.OK
        return ResponseEntity(persons, status)
    }

    @DeleteMapping("/person")
    fun personDelete(@RequestHeader(value = "Api-Key", defaultValue = "") apiKey: String,
                     @RequestHeader(value = "Content-Type", defaultValue = "") contentType: String,
                     @RequestBody(required = true) body: String): ResponseEntity<Any> {
        incrementCount()
        validateApiKey(apiKey)
        val id: Long = JsonPath.parse(body).read("$['id']")
        val personDeleted = dbPerson.deletePerson(id)
        val status: HttpStatus = if (personDeleted) HttpStatus.NO_CONTENT else HttpStatus.UNPROCESSABLE_ENTITY
        return ResponseEntity(personDeleted, status)
    }

    @PutMapping("/person")
    fun personPut(@RequestHeader(value = "Api-Key", defaultValue = "") apiKey: String,
                  @RequestHeader(value = "Content-Type", defaultValue = "") contentType: String,
                  @RequestBody(required = true) body: String): ResponseEntity<Any> {
        incrementCount()
        validateApiKey(apiKey)
        validateHeaders(contentType)
        val id: Long = JsonPath.parse(body).read("$['id']")
        val surname: String = JsonPath.parse(body).read("$['surname']")
        val name: String = JsonPath.parse(body).read("$['name']")
        val lastname: String = JsonPath.parse(body).read("$['lastname']")
        val birthDate: Date = SimpleDateFormat("yyyy-MM-dd").parse(JsonPath.parse(body).read("$['birthdate']"))
        dbPerson.updatePerson(id, surname, name, lastname, birthDate)
        return ResponseEntity(Person(id, surname, name, lastname, birthDate), HttpStatus.OK)
    }

    @PostMapping("/person")
    fun personPost(@RequestHeader(value = "Api-Key", defaultValue = "") apiKey: String,
                   @RequestHeader(value = "Content-Type", defaultValue = "") contentType: String,
                   @RequestBody(required = true) body: String): ResponseEntity<Any> {
        incrementCount()
        validateApiKey(apiKey)
        validateHeaders(contentType)

        val surname: String = JsonPath.parse(body).read("$['surname']")
        val name: String = JsonPath.parse(body).read("$['name']")
        val lastname: String = JsonPath.parse(body).read("$['lastname']")
        val birthDate: Date = SimpleDateFormat("yyyy-MM-dd").parse(JsonPath.parse(body).read("$['birthdate']"))
        val id: Long = dbPerson.createPerson(surname, name, lastname, birthDate)
        return ResponseEntity(Person(id, surname, name, lastname, birthDate), HttpStatus.CREATED)
    }
}