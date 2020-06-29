package com.example.demo

import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.StatusCode
import org.eclipse.jetty.websocket.api.WriteCallback
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage
import org.eclipse.jetty.websocket.api.annotations.WebSocket
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@WebSocket(maxTextMessageSize = 64 * 1024)
class SecureClientSocket {
    private val closeLatch: CountDownLatch
    private var session: Session? = null

    @Throws(InterruptedException::class)
    fun awaitClose(duration: Int, unit: TimeUnit?): Boolean {
        return closeLatch.await(duration.toLong(), unit)
    }

    @OnWebSocketClose
    fun onClose(statusCode: Int, reason: String?) {
        System.out.printf("Connection closed: %d - %s%n", statusCode, reason)
        session = null
        closeLatch.countDown() // trigger latch
    }

    @OnWebSocketConnect
    fun onConnect(session: Session) {
        System.out.printf("Got connect: %s%n", session)
        this.session = session
        try {

            val subscribeMessage = """{
                "type": "subscribe",
                "product_ids": [
                "BTC-USD"
                ],
                "channels": [
                "full",
                {
                    "name": "ticker",
                    "product_ids": [
                    "BTC-USD"
                    ]
                }
                ]
            }"""

            session.remote.sendString(subscribeMessage, object : WriteCallback {
                override fun writeSuccess() {
                    println("Successfully subscribed to wss feed")
                }

                override fun writeFailed(x: Throwable?) {
                    println("FAILED to subscribe wss feed")
                }
            })
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    @OnWebSocketMessage
    fun onMessage(msg: String) {
        System.out.printf("Got msg: %s%n", msg)
        if (msg.contains("Thanks")) {
            session!!.close(StatusCode.NORMAL, "I'm done")
        }
    }

    @OnWebSocketError
    fun onError(cause: Throwable) {
        print("WebSocket Error: ")
        cause.printStackTrace(System.out)
    }

    init {
        closeLatch = CountDownLatch(1000)
    }
}
