package io.inventi.tech.streams.ws.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import io.inventi.tech.streams.ingest.exchanges.binance.model.BinanceTickerSubscriptionMessage
import io.inventi.tech.streams.ingest.exchanges.binance.model.BinanceTickerSubscriptionMessage.Companion.btcusd
import io.inventi.tech.streams.ingest.exchanges.binance.parser.BinanceMessageParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.PongMessage
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession

@Component
class BinanceWsMessageHelper(
    private val kafkaObjectMapper: ObjectMapper,
    private val binanceMessageParser: BinanceMessageParser
) : WsMessageHelper() {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(BinanceWsMessageHelper::class.java)
    }

    override val exchange: String
        get() = "BINANCE"

    override fun subscribeToTicker(session: WebSocketSession) {
        session.sendMessage(TextMessage(kafkaObjectMapper.writeValueAsString(btcusd())))
    }

    override fun onMessageReceived(session: WebSocketSession, message: WebSocketMessage<*>) {
        with (message as TextMessage) {
            binanceMessageParser.parseToTicker(
                message.payload
            )?.let {
                logger.debug("Received ticker from $exchange")
                logger.info(it.toString())
            } ?: run {
                if (message.payload.toLowerCase() == "ping") {
                    logger.info("ping from $exchange")
                    session.sendMessage(PongMessage())
                }
            }
        }
    }
}