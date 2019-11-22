package webservice

import model.Person
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import responses.Success202
import java.text.SimpleDateFormat
import java.util.*

@RestController
class WebServicePerson : BaseService() {
    @GetMapping("/person")
    fun personGet(@RequestParam(value = "surname", defaultValue = "") surname: String,
                  @RequestParam(value = "name", defaultValue = "") name: String,
                  @RequestParam(value = "lastname", defaultValue = "") lastname: String,
                  @RequestParam(value = "id", defaultValue = "0") id: Long,
                  @RequestHeader(value = "Api-Key", required = false) apiKey: String?): ResponseEntity<Any> {
        incrementCount()
        validateApiKey(apiKey, 5)
        val persons: ArrayList<Person> = if (id != 0L) dbPerson.getPerson(id)
        else dbPerson.getPerson(surname, name, lastname)
        val status: HttpStatus = if (persons.isEmpty()) HttpStatus.NO_CONTENT else HttpStatus.OK
        return ResponseEntity(persons, status)
    }

    @DeleteMapping("/person")
    fun personDelete(@RequestHeader(value = "Api-Key", required = false) apiKey: String?,
                     @RequestHeader(value = "Content-Type", defaultValue = "") contentType: String,
                     @RequestBody(required = true) body: String): ResponseEntity<Any> {
        incrementCount()
        validateApiKey(apiKey, 15)
        validateHeaders(contentType)
        val id: Long = parseValidLongFromJson(body, "$['id']")
        if (validatePersonInDatabase(id))
            dbPerson.deletePerson(id)
        return ResponseEntity(Success202("Deleted Successfully"), HttpStatus.ACCEPTED)
    }



    @PutMapping("/person")
    fun personPut(@RequestHeader(value = "Api-Key", defaultValue = "") apiKey: String,
                  @RequestHeader(value = "Content-Type", defaultValue = "") contentType: String,
                  @RequestBody(required = true) body: String): ResponseEntity<Any> {
        incrementCount()
        validateApiKey(apiKey, 15)
        validateHeaders(contentType)
        val id: Long? = jsonParse(body, "$['id']") as Long?
        val surname: String? = jsonParse(body, "$['surname']") as String?
        val name: String? = jsonParse(body, "$['name']") as String?
        val lastname: String? = jsonParse(body, "$['lastname']") as String?
        val birthDate: Date = SimpleDateFormat("yyyy-MM-dd").parse(jsonParse(body, "$['birthdate']") as String?)
        dbPerson.updatePerson(id, surname, name, lastname, birthDate)
        return ResponseEntity(Person(id, surname, name, lastname, birthDate), HttpStatus.OK)
    }

    @PostMapping("/person")
    fun personPost(@RequestHeader(value = "Api-Key", defaultValue = "") apiKey: String,
                   @RequestHeader(value = "Content-Type", defaultValue = "") contentType: String,
                   @RequestBody(required = true) body: String): ResponseEntity<Any> {
        incrementCount()
        validateApiKey(apiKey, 15)
        validateHeaders(contentType)
        val surname: String = parseValidStringFromJson(body, "$['surname']") as String
        val name: String = parseValidStringFromJson(body, "$['name']") as String
        val lastname: String = parseValidStringFromJson(body, "$['lastname']") as String
        val birthDate: Date = SimpleDateFormat("yyyy-MM-dd").parse(validJsonParse(body, "$['birthdate']") as String)
        val id: Long = dbPerson.createPerson(surname, name, lastname, birthDate)
        return ResponseEntity(Person(id, surname, name, lastname, birthDate), HttpStatus.CREATED)
    }
}