package io.inventi.tech.streams.ws.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import io.inventi.tech.streams.ingest.exchanges.binance.model.BinanceTickerSubscribeRequest
import io.inventi.tech.streams.ingest.exchanges.binance.parser.BinanceMessageParser
import io.inventi.tech.streams.ingest.exchanges.bitfinex.model.BitfinexTickerSubscribeRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class BinanceWsHandler(
    private val kafkaObjectMapper: ObjectMapper,
    private val binanceMessageParser: BinanceMessageParser
) : TextWebSocketHandler() {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(BinanceWsHandler::class.java)
        const val EXCHANGE = "BINANCE"
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        logger.info("Connection to $EXCHANGE established successfully!")
//        session.sendMessage(TextMessage(subscribeTicker(BinanceTickerSubscribeRequest.btcusd())))
        session.sendMessage(TextMessage("""{
"method": "SUBSCRIBE",
"params":
[
"btcusdt@aggTrade",
"btcusdt@depth"
],
"id": 1
}"""))
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        logger.trace("Received message from $EXCHANGE")
        logger.trace(message.payload)
        binanceMessageParser.parseToTicker(message.payload)?.let {
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

    private fun subscribeTicker(subscribeRequest: BinanceTickerSubscribeRequest): String {
        return kafkaObjectMapper.writeValueAsString(subscribeRequest)
    }
}
