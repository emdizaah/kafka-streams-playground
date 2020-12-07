package io.inventi.tech.streams.ingest.exchanges.binance.parser

import com.fasterxml.jackson.databind.ObjectMapper
import io.inventi.tech.streams.ingest.exchanges.binance.model.BinanceTickerRecordModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


@Component
class BinanceMessageParser(private val kafkaObjectMapper: ObjectMapper) {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(BinanceMessageParser::class.java)
    }

    fun parseToTicker(payload: String): BinanceTickerRecordModel? {
        return try {
            kafkaObjectMapper.readValue(payload, BinanceTickerRecordModel::class.java)
        } catch (e: Exception) {
            null
        }
    }
}