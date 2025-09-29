package co.flyingtoaster.mind.rest_server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("co.flyingtoaster")
class WeddingServerApplication

fun main(args: Array<String>) {
	runApplication<WeddingServerApplication>(*args)
}
