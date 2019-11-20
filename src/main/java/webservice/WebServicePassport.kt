package webservice

import com.jayway.jsonpath.JsonPath
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class WebServicePassport : BaseService() {

    @GetMapping("/passport")
    fun passportGet(@RequestParam(defaultValue = "0") user_id: Int,
                    @RequestParam(defaultValue = "0") seria: Int,
                    @RequestParam(defaultValue = "0") number: Int,
                    @RequestHeader(value = "Api-Key", defaultValue = "") apiKey: String): ResponseEntity<Any> {
        incrementCount()
        validateApiKey(apiKey, 5)
        val passports = dbPassport.getPassport(user_id, seria, number)
        return ResponseEntity(passports, HttpStatus.OK)
    }

    @DeleteMapping("/passport")
    fun passportDelete(@RequestHeader(value = "Api-Key", defaultValue = "") apiKey: String,
                       @RequestHeader(value = "Content-Type", defaultValue = "") contentType: String,
                       @RequestBody(required = true) body: String): ResponseEntity<Any> {
        incrementCount()
        validateApiKey(apiKey, 15)
        val id: Int? = JsonPath.parse(body).read("$['id']")
        val seria: Int? = JsonPath.parse(body).read("$['seria']")
        val number: Int? = JsonPath.parse(body).read("$['number']")
        val isPassportDeleted = dbPassport.deletePassport(id, seria, number)
        val status: HttpStatus = if (isPassportDeleted) HttpStatus.NO_CONTENT else HttpStatus.UNPROCESSABLE_ENTITY
        return ResponseEntity(isPassportDeleted, status)
    }

}