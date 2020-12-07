package io.inventi.tech.streams.ingest.exchanges.kraken.parser

import Ask
import Buy
import Close
import HighPrice
import KrakenTickerRecordModel
import LowPrice
import NumberOfTrades
import OpenPrice
import Volume
import VolumeWeightedAveragePrice
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.inventi.tech.streams.ingest.exchanges.kraken.model.ChannelSubscriptionSuccessMessage
import io.inventi.tech.streams.ingest.exchanges.kraken.model.HeartBeat
import io.inventi.tech.streams.ingest.exchanges.kraken.model.SubscriptionSuccessMessage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.lang.Exception
import java.lang.RuntimeException
import java.math.BigDecimal

@Component
class KrakenMessageParser(private val kafkaObjectMapper: ObjectMapper) {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(KrakenMessageParser::class.java)
    }

    fun parseToTicker(v: String): KrakenTickerRecordModel? {
         try {

             val jsonNode = kafkaObjectMapper.readTree(v)
             if (isChannelSubscriptionSuccess(v) || jsonNode.isSubscriptionSuccessMessage(kafkaObjectMapper)  || jsonNode.isHeartbeatMessage(kafkaObjectMapper)) {
                 logger.debug("Not a ticker message: $v")
                 return null
             }
             val channelId = jsonNode[0].intValue()
             val tickerNode = jsonNode[1]

             return KrakenTickerRecordModel(
                     channelId = channelId,
                     ask = extractFromNode(tickerNode, "a") {
                         Ask(it.toBigDecimal(0), it.toInt(1), it.toBigDecimal(2))
                     },
                     buy = extractFromNode(tickerNode, "b") {
                         Buy(it.toBigDecimal(0), it.toInt(1), it.toBigDecimal(2))
                     },
                     close = extractFromNode(tickerNode, "c") {
                         Close(it.toBigDecimal(0), it.toBigDecimal(1))
                     },
                     volume = extractFromNode(tickerNode, "v") {
                         Volume(it.toBigDecimal(0), it.toBigDecimal(1))
                     },
                     price = extractFromNode(tickerNode, "p") {
                         VolumeWeightedAveragePrice(it.toBigDecimal(0), it.toBigDecimal(1))
                     },
                     trades = extractFromNode(tickerNode, "t") {
                         NumberOfTrades(it.toInt(0), it.toInt(1))
                     },
                     low = extractFromNode(tickerNode, "l") {
                         LowPrice(it.toBigDecimal(0), it.toBigDecimal(1))
                     },
                     high = extractFromNode(tickerNode, "h") {
                         HighPrice(it.toBigDecimal(0), it.toBigDecimal(1))
                     },
                     open = extractFromNode(tickerNode, "o") {
                         OpenPrice(it.toBigDecimal(0), it.toBigDecimal(1))
                     },
                     pair = jsonNode[jsonNode.size() - 1].toString()
             )
         } catch (e: Throwable) {
             logger.error("Failed to parse message", e)
             logger.error("Message was $v")
             return null
         }
    }

    fun <T> extractFromNode(tickerNode: JsonNode, nodeValue: String, block: (JsonNode) -> T): T {
        return tickerNode.findValue(nodeValue)?.let(block)
                ?: run { throw RuntimeException("unable to get value for $nodeValue") }
    }

    private fun isChannelSubscriptionSuccess(v: String): Boolean {
        return try {
            kafkaObjectMapper.readValue(v, ChannelSubscriptionSuccessMessage::class.java)
            true
        } catch (e: Exception) {
            false
        }
    }


}

fun JsonNode.toBigDecimal(index: Int): BigDecimal {
    return (BigDecimal(this[index].toString().slice(1..this[index].toString().length - 2)))
}

fun JsonNode.toInt(index: Int): Int {
    return this[index].toString().toInt()
}

fun JsonNode.isHeartbeatMessage(objectMapper: ObjectMapper): Boolean {
    return try {
        objectMapper.readValue(this.toString(), HeartBeat::class.java)
        true
    } catch (e: Exception) {
        false
    }
}

fun JsonNode.isSubscriptionSuccessMessage(objectMapper: ObjectMapper): Boolean {
    return try {
        objectMapper.readValue(this.toString(), SubscriptionSuccessMessage::class.java)
        true
    } catch (e: Exception) {
        false
    }
}