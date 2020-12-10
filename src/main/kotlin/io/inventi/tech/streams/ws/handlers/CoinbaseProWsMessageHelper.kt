package io.inventi.tech.streams.ws.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import io.inventi.tech.streams.ingest.exchanges.coinbasepro.model.CoinbaseProSubscribeRequest.Companion.btcusd
import io.inventi.tech.streams.ingest.exchanges.coinbasepro.parser.CoinbaseProMessageParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession

@Component
class CoinbaseProWsMessageHelper(
    private val kafkaObjectMapper: ObjectMapper, private val coinbaseProMessageParser: CoinbaseProMessageParser
) : WsMessageHelper() {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(CoinbaseProWsMessageHelper::class.java)
    }

    override val exchange: String
        get() = "COINBASE_PRO"

    override fun subscribeToTicker(session: WebSocketSession) {
        session.sendMessage(TextMessage(kafkaObjectMapper.writeValueAsString(btcusd())))
    }

    override fun onMessageReceived(session: WebSocketSession, message: WebSocketMessage<*>) {
        coinbaseProMessageParser.parseToTicker((message as TextMessage).payload)?.let {
            logger.debug("Received ticker from $exchange")
            logger.info(it.toString())
        }

    }
}