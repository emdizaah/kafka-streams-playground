package io.inventi.tech.streams.ws

import io.inventi.tech.streams.ws.handlers.BitfinexWsHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
class WebsocketConfig : WebSocketConfigurer {

    @Autowired
    lateinit var bitfinexWsHandler: BitfinexWsHandler

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(bitfinexWsHandler, "wss://api-pub.bitfinex.com/ws/2")
    }
}