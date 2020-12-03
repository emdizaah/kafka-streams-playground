package io.inventi.tech.streams.ingest.exchanges.bitfinex

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest
import org.eclipse.jetty.websocket.client.WebSocketClient
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import java.net.URI

@Component
class BitfinexWSClient(private val bitfinexSecureClientSocket: BitfinexSecureClientSocket) : InitializingBean {

    override fun afterPropertiesSet() {
//        launch()
    }

    fun launch() {
        val client = WebSocketClient()
        try {
            client.start()
            val wsUri = URI("wss://api-pub.bitfinex.com/ws/2")
            client.connect(bitfinexSecureClientSocket, wsUri, ClientUpgradeRequest())
            System.out.printf("Connecting to : %s%n", wsUri)
            while (true) { }
        } catch (t: Throwable) {
            t.printStackTrace()
        } finally {
            try {
                client.stop()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
