package webservice

import model.Person
import model.PersonResponse
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
    fun deletePerson(@RequestHeader(name = "Api-Key", required = false) apiKey: String?,
                     @RequestHeader(name = "Content-Type", defaultValue = "") contentType: String,
                     @RequestBody(required = false) requestBody: String): ResponseEntity<Any> {
        incrementCount()
        validateApiKey(apiKey, 15)
        validateHeaders(contentType)
        validateJson(requestBody)
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
            0 -> return ResponseEntity(PersonResponse("You can't use this method without parameters", ArrayList()), HttpStatus.UNPROCESSABLE_ENTITY)
            else -> throw Exception500.PersonDeleteNoImpl()
        }
        if (persons.isEmpty()) return ResponseEntity(PersonResponse("No persons matched for your request", ArrayList()), HttpStatus.NOT_FOUND)
        if (persons.size >= 2) return ResponseEntity(PersonResponse("Too many persons matched for your request", persons), HttpStatus.UNPROCESSABLE_ENTITY)
        dbPerson.deletePerson(persons[0])
        return ResponseEntity(PersonResponse("Person has been deleted from the system", persons), HttpStatus.OK)
    }

    @PostMapping("/person")
    fun postPerson(@RequestHeader(name = "Api-Key", required = false) apiKey: String?,
                   @RequestHeader(name = "Content-Type", defaultValue = "") contentType: String,
                   @RequestBody(required = false) requestBody: String): ResponseEntity<Any> {
        incrementCount()
        validateApiKey(apiKey, 15)
        validateHeaders(contentType)
        validateJson(requestBody)
        val surname = parseValidStringFromJson(requestBody, "$['surname']")
        val name = parseValidStringFromJson(requestBody, "$['name']")
        val lastname = parseValidStringFromJson(requestBody, "$['lastname']")
        validateStrings(surname, name, lastname)
        val birthdate = parseValidStringFromJson(requestBody, "$['birthdate']")
        validateBirthdate(birthdate)
        val passport = parseStringFromJson(requestBody, "$['passport']")
        validatePassport(passport)
        if (passport != null) {
            val passports = dbPassport.getPassports(passport)
            if (!passports.isEmpty())
                return ResponseEntity(PersonResponse("This passport is already in base", dbPerson.getPersons(passport)), HttpStatus.UNPROCESSABLE_ENTITY)
        }
        val person = Person(0, surname, name, lastname, birthdate, passport)
        dbPerson.addPerson(person)
        if (passport != null)
            dbPassport.addPassport(passport, person.person_id)
        return ResponseEntity(PersonResponse("Successfully added person to the system", ArrayList(listOf(person))), HttpStatus.CREATED)
    }

    @PutMapping("/person")
    fun putPerson(@RequestHeader(name = "Api-Key", required = false) apiKey: String?,
                  @RequestHeader(name = "Content-Type", defaultValue = "") contentType: String,
                  @RequestBody(required = false) requestBody: String): ResponseEntity<Any> {
        incrementCount()
        validateApiKey(apiKey, 15)
        validateHeaders(contentType)
        validateJson(requestBody)
        val personId = parseValidIntFromJson(requestBody, "$['id']")
        val surname = parseValidStringFromJson(requestBody, "$['surname']")
        val name = parseValidStringFromJson(requestBody, "$['name']")
        val lastname = parseValidStringFromJson(requestBody, "$['lastname']")
        validateStrings(surname, name, lastname)
        val birthdate = parseValidStringFromJson(requestBody, "$['birthdate']")
        validateBirthdate(birthdate)
        val passport = parseStringFromJson(requestBody, "$['passport']")
        validatePassport(passport)
        var inputCase = 0
        if (dbPerson.getPersons(personId).isEmpty())
            return ResponseEntity(PersonResponse("User with ID $personId not found", ArrayList()), HttpStatus.NOT_FOUND)
        if (passport != null) {
            inputCase += checkPassportValidity(personId, passport)
        }
        val person = Person(personId, surname, name, lastname, birthdate, passport)
        when (inputCase) {
            0 -> {
                dbPassport.deletePassport(personId)
                dbPerson.updatePerson(person)
            }
            1 -> {
                dbPassport.addPassport(passport!!, personId)
                dbPerson.updatePerson(person)
            }
            2 -> {
                dbPerson.updatePerson(person)
                dbPassport.updatePassport(passport!!, personId)
            }
            3 -> {
                dbPerson.updatePerson(person)
            }
            4 -> return ResponseEntity(PersonResponse("This password is already mapped to another person", ArrayList(listOf(person))),
                    HttpStatus.UNPROCESSABLE_ENTITY)
        }
        return ResponseEntity(PersonResponse("Successfull updated user in the system", ArrayList(listOf(person))), HttpStatus.OK)
    }

    private fun checkPassportValidity(personId: Int, passport: String): Int {
        val passports = dbPassport.getPassports(passport)
        if (passports.isEmpty()) return 1
        if (passports[0].person_id == null) return 2
        return if (passports[0].person_id == personId) 3
        else 4
    }


}