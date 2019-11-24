package webservice

import model.Person
import model.PersonDelete
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import responses.Exception500

@RestController
class WebServicePerson : BaseService() {

    @GetMapping("/person")
    fun getPersons(@RequestParam(name = "person_id", required = false) person_id: String?,
                   @RequestParam(name = "surname", required = false) surname: String?,
                   @RequestParam(name = "name", required = false) name: String?,
                   @RequestParam(name = "lastname", required = false) lastname: String?,
                   @RequestParam(name = "passport", required = false) passport: String?,
                   @RequestHeader(name = "Api-Key", required = false) apiKey: String?): ResponseEntity<Any> {
        incrementCount()
        validateApiKey(apiKey, 5)
        validatePassport(passport)
        val personId: Int? = validateParamAsInt(person_id)
        val persons: List<Person>
        var inputCase = 0
        if (surname != null || name != null || lastname != null) inputCase += 1
        if (passport != null) inputCase += 2
        if (personId != null) inputCase += 4
        persons = when (inputCase) {
            in 4..7 -> dbPerson.getPersons(personId)
            in 2..3 -> dbPerson.getPersons(passport)
            1 -> dbPerson.getPersons(surname, name, lastname)
            0 -> dbPerson.getPersons()
            else -> throw Exception500.PersonGetNoImpl()
        }
        return ResponseEntity(persons, HttpStatus.OK)
    }

    @DeleteMapping("/person")
    fun deletePersons(@RequestHeader(name = "Api-Key", required = false) apiKey: String?,
                      @RequestHeader(name = "Content-Type", defaultValue = "") contentType: String,
                      @RequestBody(required = false) requestBody: String): ResponseEntity<Any> {
        incrementCount()
        validateApiKey(apiKey, 15)
        val personId = parseIntFromJson(requestBody, "$['id']")
        val surname = parseStringFromJson(requestBody, "$['surname']")
        val name = parseStringFromJson(requestBody, "$['name']")
        val lastname = parseStringFromJson(requestBody, "$['lastname']")
        val passport = parseStringFromJson(requestBody, "$['passport']")
        validatePassport(passport)
        val persons: List<Person>
        var inputCase = 0
        if (surname != null || name != null || lastname != null) inputCase += 1
        if (passport != null) inputCase += 2
        if (personId != null) inputCase += 4
        persons = when (inputCase) {
            in 4..7 -> dbPerson.getPersons(personId)
            in 2..3 -> dbPerson.getPersons(passport)
            1 -> dbPerson.getPersons(surname, name, lastname)
            0 -> return ResponseEntity(PersonDelete("You can't use this method without parameters", ArrayList()), HttpStatus.UNPROCESSABLE_ENTITY)
            else -> throw Exception500.PersonDeleteNoImpl()
        }
        if (persons.isEmpty()) return ResponseEntity(PersonDelete("No persons matched for your request", ArrayList()), HttpStatus.NOT_FOUND)
        if (persons.size >= 2) return ResponseEntity(PersonDelete("Too many persons matched for your request", persons), HttpStatus.UNPROCESSABLE_ENTITY)
        dbPerson.deletePerson(persons[0])
        return ResponseEntity(PersonDelete("Person has been deleted from the system", persons), HttpStatus.OK)
    }

}