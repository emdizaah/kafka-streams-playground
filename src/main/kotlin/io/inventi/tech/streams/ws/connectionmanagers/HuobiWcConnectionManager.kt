package io.inventi.tech.streams.ws.connectionmanagers

import io.inventi.tech.streams.ws.handlers.BinanceWsHandler
import io.inventi.tech.streams.ws.handlers.HoubiWsHandler
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import org.springframework.web.socket.client.WebSocketConnectionManager
import org.springframework.web.socket.client.standard.StandardWebSocketClient

@Component
class HuobiWcConnectionManager(
    private val huobiWsHandler: HoubiWsHandler
) : InitializingBean {

    companion object {
        const val EXCHANGE = "HUOBI"
    }

    override fun afterPropertiesSet() {
        println("connecting to $EXCHANGE...")
        val webSocketConnectionManager = WebSocketConnectionManager(StandardWebSocketClient(), huobiWsHandler, "wss://api.huobi.pro/ws")
        webSocketConnectionManager.start()
    }

}