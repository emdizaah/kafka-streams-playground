package io.inventi.tech.streams.ingest.exchanges.bitfinex.parser

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.inventi.tech.streams.ingest.exchanges.bitfinex.model.BitfinexTickerRecordModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.asm.TypeReference
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Component
import java.math.BigDecimal


@Component
class BitfinexMessageParser(private val kafkaObjectMapper: ObjectMapper) {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(BitfinexMessageParser::class.java)
    }

    fun parseToTicker(v: String): BitfinexTickerRecordModel? {
        return try {
            val arrayReader = kafkaObjectMapper.readerForArrayOf(String::class.java)
            val values = arrayReader.readTree(v)
            val symbol = values[0].toString()
            val tickerValues = arrayReader.readTree(values[1].toString())
            BitfinexTickerRecordModel(
                symbol = symbol,
                bid = fromNode(tickerValues, 0),
                bidSize = fromNode(tickerValues, 1),
                ask = fromNode(tickerValues, 2),
                askSize = fromNode(tickerValues, 3),
                dailyChange = fromNode(tickerValues, 4),
                dailyChangeRelative = fromNode(tickerValues, 5),
                lastPrice = fromNode(tickerValues, 6),
                volume = fromNode(tickerValues, 7),
                high = fromNode(tickerValues, 8),
                low = fromNode(tickerValues, 9)
            )
        } catch (e: Exception) {
            logger.debug("Not a ticker message")
            logger.debug("Message was $v")
            null
        }
    }

    private fun fromNode(jsonNode: JsonNode, index: Int): BigDecimal {
        return BigDecimal(jsonNode[index].toString())
    }
}