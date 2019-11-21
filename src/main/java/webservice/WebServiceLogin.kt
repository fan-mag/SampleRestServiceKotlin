package webservice

import model.ApiKey
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
open class WebServiceLogin : BaseService() {
    @PutMapping("/login")
    fun getApiKey(@RequestBody(required = true) body: String,
                  @RequestHeader(value = "Content-Type") contentType: String): ResponseEntity<Any> {
        incrementCount()
        validateHeaders(contentType)
        val login: String = parseValidStringFromJson(body, "$['login']")
        val password: String = parseValidStringFromJson(body, "$['password']")
        return ResponseEntity(ApiKey(dbCredentials.getApiKey(login, password)), HttpStatus.OK)
    }

    @PostMapping("/login")
    fun postApiKey(@RequestBody(required = true) body: String,
                   @RequestHeader(value = "Content-Type", defaultValue = "") contentType: String): ResponseEntity<Any> {
        incrementCount()
        validateHeaders(contentType)
        val login: String = parseValidStringFromJson(body, "$['login']")
        val password: String = parseValidStringFromJson(body, "$['password']")
        return ResponseEntity(ApiKey(dbCredentials.generateApiKey(login, password)), HttpStatus.ACCEPTED)
    }
}