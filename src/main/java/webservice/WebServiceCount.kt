package webservice

import model.Count
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class WebServiceCount : BaseService() {
    @GetMapping("/count")
    fun countGet(@RequestHeader(value = "Api-Key", defaultValue = "") apiKey: String) : ResponseEntity<Any> {
        db.incrementCount()
        db.validateApiKey(apiKey)
        return ResponseEntity(Count(db.getCount()), HttpStatus.OK)
    }
}