package io.inventi.tech.streams.ws.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import io.inventi.tech.streams.ingest.exchanges.binance.parser.BinanceMessageParser
import io.inventi.tech.streams.ingest.exchanges.bitfinex.model.BitfinexTickerSubscribeRequest
import io.inventi.tech.streams.ingest.exchanges.bitfinex.model.BitfinexTickerSubscribeRequest.Companion.btcusd
import io.inventi.tech.streams.ingest.exchanges.bitfinex.parser.BitfinexMessageParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession

@Component
class BitfinexWsMessageHelper(
    private val kafkaObjectMapper: ObjectMapper,
    private val bitfinexMessageParser: BitfinexMessageParser
) : WsMessageHelper() {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(BitfinexWsMessageHelper::class.java)
    }

    override val exchange: String
        get() = "BITFINEX"

    override fun subscribeToTicker(session: WebSocketSession) {
        session.sendMessage(TextMessage(kafkaObjectMapper.writeValueAsString(btcusd())))
    }

    override fun onMessageReceived(session: WebSocketSession, message: WebSocketMessage<*>) {
        bitfinexMessageParser.parseToTicker((message as TextMessage).payload)?.let {
            logger.debug("Received ticker from $exchange")
            logger.info(it.toString())
        }
    }
}