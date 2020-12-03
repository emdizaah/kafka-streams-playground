package io.inventi.tech.streams.ws.connectionmanagers

import io.inventi.tech.streams.ws.handlers.BitfinexWsHandler
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import org.springframework.web.socket.client.WebSocketConnectionManager
import org.springframework.web.socket.client.standard.StandardWebSocketClient

@Component
class BitfinexWsConnectionManager(
        val bitfinexWsHandler: BitfinexWsHandler
) : InitializingBean {

    companion object {
        const val EXCHANGE = "BITFINEX"
    }

    override fun afterPropertiesSet() {
        println("connecting to $EXCHANGE...")
        val webSocketConnectionManager = WebSocketConnectionManager(StandardWebSocketClient(), bitfinexWsHandler, "wss://api-pub.bitfinex.com/ws/2")
        webSocketConnectionManager.start()
    }
}