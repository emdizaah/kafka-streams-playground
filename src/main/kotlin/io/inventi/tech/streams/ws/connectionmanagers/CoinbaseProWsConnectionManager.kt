package io.inventi.tech.streams.ws.connectionmanagers

import io.inventi.tech.streams.ws.handlers.CoinbaseProWsHandler
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.client.WebSocketConnectionManager
import org.springframework.web.socket.client.standard.StandardWebSocketClient

@Configuration
class CoinbaseProWsConnectionManager(private val conbaseProWsHandler: CoinbaseProWsHandler) : InitializingBean {
    companion object {
        const val EXCHANGE = "COINBASE_PRO"
    }

    override fun afterPropertiesSet() {

        println("connecting to $EXCHANGE...")
        val webSocketConnectionManager = WebSocketConnectionManager(
            StandardWebSocketClient(),
            conbaseProWsHandler,
            "wss://ws-feed.pro.coinbase.com"
        )

        webSocketConnectionManager.start()
    }
}