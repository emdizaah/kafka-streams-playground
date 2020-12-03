package io.inventi.tech.streams.ws.connectionmanagers

import io.inventi.tech.streams.ws.handlers.KrakenWsHandler
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import org.springframework.web.socket.client.WebSocketConnectionManager
import org.springframework.web.socket.client.standard.StandardWebSocketClient

@Component
class KrakenWsConnectionManager(private val krakenWsHandler: KrakenWsHandler) : InitializingBean {

    companion object {
        const val EXCHANGE = "KRAKEN"
    }

    override fun afterPropertiesSet() {
        println("Connecting to $EXCHANGE...")
        val webSocketConnectionManager = WebSocketConnectionManager(StandardWebSocketClient(), krakenWsHandler, "wss://beta-ws.kraken.com")
        webSocketConnectionManager.start()
    }
}