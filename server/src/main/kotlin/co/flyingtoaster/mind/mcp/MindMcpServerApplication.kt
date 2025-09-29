package co.flyingtoaster.mind.mcp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("co.flyingtoaster")
class MindMcpServerApplication

fun main(args: Array<String>) {
	runApplication<MindMcpServerApplication>(*args)
}
