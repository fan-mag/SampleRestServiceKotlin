package webservice

import model.Person
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
        when (inputCase) {
            in 4..7 -> persons = dbPerson.getPersons(personId)
            in 2..3 -> persons = dbPerson.getPersons(passport)
            1 -> persons = dbPerson.getPersons(surname, name, lastname)
            0 -> persons = dbPerson.getPersons()
            else -> throw Exception500.PersonGetNoImpl()
        }
        return ResponseEntity(persons, HttpStatus.OK)
    }

}