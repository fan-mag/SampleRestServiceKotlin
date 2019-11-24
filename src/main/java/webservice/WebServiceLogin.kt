package webservice

import model.ApiKey
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
open class WebServiceLogin : BaseService() {
    @PutMapping("/login")
    fun getApiKey(@RequestBody(required = true) requestBody: String,
                  @RequestHeader(value = "Content-Type") contentType: String): ResponseEntity<Any> {
        incrementCount()
        validateHeaders(contentType)
        validateJson(requestBody)
        val login: String = parseValidStringFromJson(requestBody, "$['login']")
        val password: String = parseValidStringFromJson(requestBody, "$['password']")
        return ResponseEntity(ApiKey(dbCredentials.getApiKey(login, password)), HttpStatus.OK)
    }

    @PostMapping("/login")
    fun postApiKey(@RequestBody(required = true) requestBody: String,
                   @RequestHeader(value = "Content-Type", defaultValue = "") contentType: String,
                   @RequestHeader(value = "Api-Key", required = false) apiKey: String?): ResponseEntity<Any> {
        incrementCount()
        validateHeaders(contentType)
        validateApiKey(apiKey, 5)
        validateJson(requestBody)
        val login: String = parseValidStringFromJson(requestBody, "$['login']")
        val password: String = parseValidStringFromJson(requestBody, "$['password']")
        return ResponseEntity(ApiKey(dbCredentials.generateApiKey(login, password)), HttpStatus.ACCEPTED)
    }
}