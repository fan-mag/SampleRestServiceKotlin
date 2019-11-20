package webservice

import com.jayway.jsonpath.JsonPath
import model.ApiKey
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController


@RestController
open class WebServiceLogin : BaseService() {
    @PutMapping("/login")
    fun getApiKey(@RequestBody(required = true) body: String,
                  @RequestHeader(value = "Content-Type") contentType: String): ResponseEntity<Any> {
        db.incrementCount()
        validateHeaders(contentType)
        val login: String = JsonPath.parse(body).read("$['login']")
        val password: String = JsonPath.parse(body).read("$['password']")
        return ResponseEntity(ApiKey(db.getApiKey(login, password)), HttpStatus.OK)
    }
}