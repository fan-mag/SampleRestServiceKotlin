package webservice

import model.Welcome
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.TimeZone
import java.text.SimpleDateFormat

@RestController
class WebServiceWelcome : BaseService() {
    @GetMapping("/")
    fun countGet() : ResponseEntity<Any> {
        incrementCount()
        val message = "Welcome to Passport Table rest service"
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        dateFormat.timeZone = TimeZone.getTimeZone("UTC+3")
        return ResponseEntity(Welcome(message, dateFormat.format(startDate)), HttpStatus.OK)
    }

}