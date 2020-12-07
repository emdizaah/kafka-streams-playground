package io.inventi.tech.streams.ws.connectionmanagers

import io.inventi.tech.streams.ws.handlers.BinanceWsHandler
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.client.WebSocketConnectionManager
import org.springframework.web.socket.client.standard.StandardWebSocketClient

@Configuration
class BinanceWsConnectionManager(private val binanceWsHandler: BinanceWsHandler) : InitializingBean {
    companion object {
        const val EXCHANGE = "BINANCE"
    }

    override fun afterPropertiesSet() {

        println("connecting to $EXCHANGE...")
        val webSocketConnectionManager = WebSocketConnectionManager(
            StandardWebSocketClient(),
            binanceWsHandler,
            "wss://fstream.binance.com/ws/stream?streams=btcusd@ticker"
        )

        webSocketConnectionManager.start()
    }
}