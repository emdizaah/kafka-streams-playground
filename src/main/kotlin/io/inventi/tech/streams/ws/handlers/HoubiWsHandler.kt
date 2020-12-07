package io.inventi.tech.streams.ws.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import io.inventi.tech.streams.ingest.exchanges.huobi.model.HuobiTickerSubscribeRequest
import io.inventi.tech.streams.ingest.exchanges.huobi.model.HuobiTickerSubscribeRequest.Companion.btcusd
import io.inventi.tech.streams.ingest.exchanges.huobi.parser.HuobiMessageParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.BinaryMessage
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.BinaryWebSocketHandler
import java.io.ByteArrayInputStream
import java.util.zip.GZIPInputStream

@Component
class HoubiWsHandler(
    private val kafkaObjectMapper: ObjectMapper,
    private val huobiMessageParser: HuobiMessageParser
) : BinaryWebSocketHandler() {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(HoubiWsHandler::class.java)
        const val EXCHANGE = "HUOBI"
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        logger.info("Connection to $EXCHANGE established successfully!")
        session.sendMessage(TextMessage(subscribeTicker(btcusd())))
    }

    override fun handleBinaryMessage(session: WebSocketSession, message: BinaryMessage) {
        val payload = message.payload
        val bytes = ByteArray(message.payload.remaining())
        payload.get(bytes)
        val message = GZIPInputStream(ByteArrayInputStream(bytes)).bufferedReader().use { it.readText() }
        logger.info(message)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        logger.info("Received message from $EXCHANGE")
        logger.info(message.payload)
        huobiMessageParser.parseToTicker(message.payload)?. let {
            logger.info("Received ticker from $EXCHANGE")
            logger.info(it.toString())
        }

    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        logger.error("Failed to read message from $EXCHANGE", exception)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        logger.warn("Connection to $EXCHANGE was closed")
    }

    private fun subscribeTicker(subscribeRequest: HuobiTickerSubscribeRequest): String {
        return kafkaObjectMapper.writeValueAsString(subscribeRequest)
    }
}