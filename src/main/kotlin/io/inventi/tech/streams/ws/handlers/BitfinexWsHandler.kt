package io.inventi.tech.streams.ws.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import io.inventi.tech.streams.ingest.exchanges.bitfinex.model.BitfinexTickerSubscribeRequest
import io.inventi.tech.streams.ingest.exchanges.bitfinex.model.BitfinexTickerSubscribeRequest.Companion.btcusd
import io.inventi.tech.streams.ingest.exchanges.bitfinex.parser.BitfinexMessageParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class BitfinexWsHandler(
        private val kafkaObjectMapper: ObjectMapper,
        private val bitfinexMessageParser: BitfinexMessageParser
) : TextWebSocketHandler() {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(BitfinexWsHandler::class.java)
        const val EXCHANGE = "BITFINEX"
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        logger.info("Connection to $EXCHANGE established successfully!")
        session.sendMessage(TextMessage(subscribeTicker(btcusd())))
    }
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        logger.trace("Received message from $EXCHANGE")
        logger.trace(message.payload)
        bitfinexMessageParser.parseToTicker(message.payload)?. let {
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

    private fun subscribeTicker(subscribeRequestBitfinex: BitfinexTickerSubscribeRequest): String {
        return kafkaObjectMapper.writeValueAsString(subscribeRequestBitfinex)
    }
}