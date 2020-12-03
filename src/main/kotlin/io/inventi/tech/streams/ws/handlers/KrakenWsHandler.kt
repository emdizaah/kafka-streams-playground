package io.inventi.tech.streams.ws.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import io.inventi.tech.streams.ingest.exchanges.bitfinex.model.BitfinexTickerSubscribeRequest
import io.inventi.tech.streams.ingest.exchanges.kraken.model.KrakenTickerSubscribeRequest
import io.inventi.tech.streams.ingest.exchanges.kraken.model.KrakenTickerSubscribeRequest.Companion.btcusd
import io.inventi.tech.streams.ingest.exchanges.kraken.parser.KrakenMessageParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class KrakenWsHandler(private val kafkaObjectMapper: ObjectMapper, private val krakenMessageParser: KrakenMessageParser) : TextWebSocketHandler() {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(KrakenWsHandler::class.java)
        const val EXCHANGE = "KRAKEN"
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        logger.info("Connection to $EXCHANGE established successfully!")
        session.sendMessage(TextMessage(subscribeTicker(btcusd())))
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        logger.trace("Received message from $EXCHANGE")
        logger.trace(message.payload)
        krakenMessageParser.parseToTicker(message.payload) ?. let {
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

    private fun subscribeTicker(subscribeRequestKraken: KrakenTickerSubscribeRequest): String {
        return kafkaObjectMapper.writeValueAsString(subscribeRequestKraken)
    }
}