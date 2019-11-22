package webservice


import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@RestController
open class WebServiceApplication : BaseService() {

    companion object {
        private lateinit var application: ConfigurableApplicationContext

        @JvmStatic
        fun main(args: Array<String>) {
            application = SpringApplication.run(WebServiceApplication::class.java)
        }
    }
}
