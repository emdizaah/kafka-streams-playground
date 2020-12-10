package io.inventi.tech.streams.ws.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import io.inventi.tech.streams.ingest.exchanges.kraken.model.KrakenTickerSubscribeRequest.Companion.btcusd
import io.inventi.tech.streams.ingest.exchanges.kraken.parser.KrakenMessageParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession

@Component
class KrakenWsMessageHelper(
    private val kafkaObjectMapper: ObjectMapper,
    private val krakenMessageParser: KrakenMessageParser
) : WsMessageHelper() {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(KrakenWsMessageHelper::class.java)
    }

    override val exchange: String
        get() = "KRAKEN"

    override fun subscribeToTicker(session: WebSocketSession) {
        session.sendMessage(TextMessage(kafkaObjectMapper.writeValueAsString(btcusd())))
    }

    override fun onMessageReceived(session: WebSocketSession, message: WebSocketMessage<*>) {
        krakenMessageParser.parseToTicker((message as TextMessage).payload)?.let {
            logger.debug("Received ticker from $exchange")
            logger.info(it.toString())
        }
    }
}


