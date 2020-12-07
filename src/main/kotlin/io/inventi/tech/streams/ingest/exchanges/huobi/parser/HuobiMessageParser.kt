package io.inventi.tech.streams.ingest.exchanges.huobi.parser

import com.fasterxml.jackson.databind.ObjectMapper
import io.inventi.tech.streams.ingest.exchanges.huobi.model.HuobiTickerRecordModel
import org.springframework.stereotype.Component


@Component
class HuobiMessageParser(
    private val kafkaObjectMapper: ObjectMapper
) {
    fun parseToTicker(v: String): HuobiTickerRecordModel? {
        return try {
            kafkaObjectMapper.readValue(v, HuobiTickerRecordModel::class.java)
        } catch (e: Exception) {
            return null
        }
    }
}