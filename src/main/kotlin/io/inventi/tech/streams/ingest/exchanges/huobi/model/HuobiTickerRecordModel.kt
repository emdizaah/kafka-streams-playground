package io.inventi.tech.streams.ingest.exchanges.huobi.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class HuobiTickerRecordModel(
    @JsonProperty("ch") val channel: String,
    @JsonProperty("ts") val timestamp: Long,
    @JsonProperty("tick") val ticker: Ticker
)

data class Ticker(
    val amount: BigDecimal,
    val open: BigDecimal,
    val close: BigDecimal,
    val high: BigDecimal,
    val id: Long,
    val count: Long,
    val low: BigDecimal,
    val version: Long,
    @JsonProperty("vol") val volume: BigDecimal
)