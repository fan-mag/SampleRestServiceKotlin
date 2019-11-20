package webservice

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

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

}