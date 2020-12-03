package io.inventi.tech.streams.ingest.exchanges.kraken

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest
import org.eclipse.jetty.websocket.client.WebSocketClient
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import java.net.URI

@Component
class KrakenWSClient(private val krakenSecureClientSocket: KrakenSecureClientSocket) : InitializingBean {

    override fun afterPropertiesSet() {
//        launch()
    }

    fun launch() {
        val client = WebSocketClient()
        try {
            client.start()
            val wsUri = URI("wss://beta-ws.kraken.com")
            client.connect(krakenSecureClientSocket, wsUri, ClientUpgradeRequest())
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
