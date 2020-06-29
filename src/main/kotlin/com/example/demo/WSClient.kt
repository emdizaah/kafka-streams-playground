package com.example.demo

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest
import org.eclipse.jetty.websocket.client.WebSocketClient
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import java.net.URI
import java.util.concurrent.TimeUnit

@Component
class WSClient : InitializingBean {

    override fun afterPropertiesSet() {
        launch()
    }

    fun launch() {
        var destUri = "wss://ws-feed-public.sandbox.pro.coinbase.com"

        val client = WebSocketClient()
        val socket = SecureClientSocket()
        try {
            client.start()
            val echoUri = URI(destUri)
            val request = ClientUpgradeRequest()
            client.connect(socket, echoUri, request)
            System.out.printf("Connecting to : %s%n", echoUri)
            while (true) { }

            // wait for closed socket connection.
            socket.awaitClose(5, TimeUnit.SECONDS)
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
