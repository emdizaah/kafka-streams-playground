package io.inventi.tech.streams.ingest

import com.fasterxml.jackson.databind.ObjectMapper
import io.inventi.tech.streams.model.BookRecordUpdate
import io.inventi.tech.streams.model.BookRecordUpdateIncoming
import io.inventi.tech.streams.model.fromIncoming
import io.inventi.tech.streams.parser.CryptoDataParser
import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.WriteCallback
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage
import org.eclipse.jetty.websocket.api.annotations.WebSocket
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@WebSocket(maxTextMessageSize = 64 * 1024)
@Component
class SecureClientSocket(
    private val cryptoDataProducerTemplate: KafkaTemplate<String, BookRecordUpdate>,
    private val cryproDataParser: CryptoDataParser,
    private val kafkaObjectMapper: ObjectMapper
) {

    companion object {
        val logger = LoggerFactory.getLogger(SecureClientSocket::class.java)
    }

    private val closeLatch: CountDownLatch
    private var session: Session? = null

    @Throws(InterruptedException::class)
    fun awaitClose(duration: Int, unit: TimeUnit?): Boolean {
        return closeLatch.await(duration.toLong(), unit)
    }

    @OnWebSocketClose
    fun onClose(statusCode: Int, reason: String?) {
        logger.info("Connection closed: %d - %s%n", statusCode, reason)
        session = null
        closeLatch.countDown() // trigger latch
    }

    @OnWebSocketConnect
    fun onConnect(session: Session) {
        logger.info("Got connect: %s%n", session)
        this.session = session
        try {

            val subscribeMessage = """{
              "event": "subscribe",
              "pair": [
                "XBT/USD",
                "ETH/USD",
                "BCH/USD",
                "XRP/USD"
              ],
              "subscription": {
                "name": "book"
              }
            }"""

            session.remote.sendString(subscribeMessage, object : WriteCallback {
                override fun writeSuccess() {
                    logger.info("Successfully subscribed to wss feed")
                }

                override fun writeFailed(x: Throwable?) {
                    logger.error("FAILED to subscribe wss feed")
                }
            })
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    @OnWebSocketMessage
    fun onMessage(msg: String) {
        if (!msg.startsWith("[")) return
        val bookRecordUpdateIncoming = cryproDataParser.parseToBookEvent(msg) ?: return
        cryptoDataProducerTemplate.send("crypto", fromIncoming(bookRecordUpdateIncoming)).addCallback({
            logger.debug("Submitted successfully:\n$msg")
        }, {
            logger.error("Failed to submit")
        })
    }

    @OnWebSocketError
    fun onError(cause: Throwable) {
        logger.error("WebSocket Error: ")
        cause.printStackTrace(System.out)
    }

    init {
        closeLatch = CountDownLatch(1000)
    }
}
