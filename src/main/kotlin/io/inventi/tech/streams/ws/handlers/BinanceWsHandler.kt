package io.inventi.tech.streams.ws.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import io.inventi.tech.streams.ingest.exchanges.binance.model.BinanceTickerSubscriptionMessage
import io.inventi.tech.streams.ingest.exchanges.binance.model.BinanceTickerSubscriptionMessage.Companion.btcusd
import io.inventi.tech.streams.ingest.exchanges.binance.parser.BinanceMessageParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.*
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
        logger.debug("Connection to $EXCHANGE established successfully!")
        subscribeToTicker(session)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        logger.debug("Got message from $EXCHANGE: ${message.payload}")
        handleTextMessage(message, session)
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        logger.error("Failed to read message from $EXCHANGE", exception)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        logger.warn("Connection to $EXCHANGE was closed")
    }

    private fun handleTextMessage(message: TextMessage, session: WebSocketSession) {

        binanceMessageParser.parseToTicker(
            message.payload
        )?.let {
            logger.debug("Received ticker from $EXCHANGE")
            logger.info(it.toString())
        } ?: run {
            if (message.payload.toLowerCase() == "ping") {
                logger.info("ping from $EXCHANGE")
                session.sendMessage(PongMessage())
            }
        }
    }


    private fun subscribeToTicker(session: WebSocketSession) {
        session.sendMessage(TextMessage(kafkaObjectMapper.writeValueAsString(btcusd())))
    }
}
