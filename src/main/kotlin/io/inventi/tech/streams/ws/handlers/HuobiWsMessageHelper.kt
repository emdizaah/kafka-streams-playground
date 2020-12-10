package io.inventi.tech.streams.ws.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import io.inventi.tech.streams.ingest.exchanges.huobi.model.HuobiTickerSubscribeRequest.Companion.btcusd
import io.inventi.tech.streams.ingest.exchanges.huobi.model.PingMessage
import io.inventi.tech.streams.ingest.exchanges.huobi.model.PongMessage
import io.inventi.tech.streams.ingest.exchanges.huobi.parser.HuobiMessageParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.BinaryMessage
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession
import java.io.ByteArrayInputStream
import java.util.zip.GZIPInputStream

@Component
class HuobiWsMessageHelper(
    private val kafkaObjectMapper: ObjectMapper, private val huobiMessageParser: HuobiMessageParser
) : WsMessageHelper() {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(HuobiWsMessageHelper::class.java)
    }

    override val exchange: String
        get() = "HUOBI"

    override fun subscribeToTicker(session: WebSocketSession) {
        session.sendMessage(TextMessage(kafkaObjectMapper.writeValueAsString(btcusd())))
    }

    override fun onMessageReceived(session: WebSocketSession, message: WebSocketMessage<*>) {
        val stringMessage = parseToString(message as BinaryMessage)
        parseToPingMessage(stringMessage)?.let {
            sendPong(session, it)
        } ?: run {

            huobiMessageParser.parseToTicker(stringMessage)?.let {
                logger.debug("Received ticker from $exchange")
                logger.info(it.toString())
            }
        }
    }

    private fun parseToPingMessage(message: String): PingMessage? {
        return try {
            kafkaObjectMapper.readValue(message, PingMessage::class.java)
        } catch (e: Exception) {
            null
        }
    }

    private fun sendPong(session: WebSocketSession, pingMessage: PingMessage) {
        session.sendMessage(TextMessage(kafkaObjectMapper.writeValueAsString(PongMessage(pong = pingMessage.ping))))
    }

    private fun parseToString(message: BinaryMessage): String {
        val payload = message.payload
        val bytes = ByteArray(message.payload.remaining())
        payload.get(bytes)
        return GZIPInputStream(ByteArrayInputStream(bytes)).bufferedReader().use { it.readText() }
    }
}