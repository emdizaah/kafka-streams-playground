package io.inventi.tech.streams

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.socket.config.annotation.EnableWebSocket

@SpringBootApplication
@EnableWebSocket
class Application

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
