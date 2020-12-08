package io.inventi.tech.streams.ws.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import io.inventi.tech.streams.ingest.exchanges.coinbasepro.model.CoinbaseProSubscribeRequest
import io.inventi.tech.streams.ingest.exchanges.coinbasepro.model.CoinbaseProSubscribeRequest.Companion.btcusd
import io.inventi.tech.streams.ingest.exchanges.coinbasepro.parser.CoinbaseProMessageParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class CoinbaseProWsHandler(
    private val kafkaObjectMapper: ObjectMapper,
    private val coinbaseProMessageParser: CoinbaseProMessageParser
) : TextWebSocketHandler() {

    companion object {
        const val EXCHANGE = "COINBASE_PRO"
        val logger: Logger = LoggerFactory.getLogger(CoinbaseProWsHandler::class.java)
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        logger.info("Connection to $EXCHANGE established successfully!")
        subscribeToTicker(session)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        logger.debug("Got message from $EXCHANGE: ${message.payload}")
        handleTextMessage(message)
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        logger.error("Failed to read message from $EXCHANGE", exception)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        logger.warn("Connection to $EXCHANGE was closed")
    }

    private fun handleTextMessage(message: TextMessage) {
        coinbaseProMessageParser.parseToTicker(message.payload)?.let {
            logger.debug("Received ticker from $EXCHANGE")
            logger.info(it.toString())
        }
    }

    private fun subscribeToTicker(session: WebSocketSession) {
        session.sendMessage(TextMessage(kafkaObjectMapper.writeValueAsString(btcusd())))
    }
}