package io.inventi.tech.streams.ingest.exchanges.coinbasepro.parser

import com.fasterxml.jackson.databind.ObjectMapper
import io.inventi.tech.streams.ingest.exchanges.coinbasepro.model.CoinbaseProTickerRecordModel
import org.springframework.stereotype.Component
import java.lang.Exception

@Component
class CoinbaseProMessageParser(
    private val kafkaObjectMapper: ObjectMapper
) {
    fun parseToTicker(v: String): CoinbaseProTickerRecordModel? {
        return try {
            return kafkaObjectMapper.readValue(v, CoinbaseProTickerRecordModel::class.java)
        } catch (e: Exception) {
            return null
        }
    }
}