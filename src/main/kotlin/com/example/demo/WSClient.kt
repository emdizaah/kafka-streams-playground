package com.example.demo

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest
import org.eclipse.jetty.websocket.client.WebSocketClient
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import java.net.URI

@Component
class WSClient(private val secureClientSocket: SecureClientSocket) : InitializingBean {

    override fun afterPropertiesSet() {
        launch()
    }

    fun launch() {
        val client = WebSocketClient()
        try {
            client.start()
            val wsUri = URI("wss://ws-feed-public.sandbox.pro.coinbase.com")
            client.connect(secureClientSocket, wsUri, ClientUpgradeRequest())
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
