package webservice

import com.jayway.jsonpath.JsonPath
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
        val login: String? = jsonParse(body, "$['login']") as String?
        val password: String? = jsonParse(body, "$['password']") as String?
        return ResponseEntity(ApiKey(dbCredentials.getApiKey(login, password)), HttpStatus.OK)
    }

    @PostMapping("/login")
    fun postApiKey(@RequestBody(required = true) body: String,
                   @RequestHeader(value = "Content-Type") contentType: String): ResponseEntity<Any> {
        incrementCount()
        validateHeaders(contentType)
        val login: String? = jsonParse(body, "$['login']") as String?
        val password: String? = jsonParse(body, "$['password']") as String?
        return ResponseEntity(ApiKey(dbCredentials.generateApiKey(login, password)), HttpStatus.OK)
    }
}