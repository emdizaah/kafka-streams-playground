package io.inventi.tech.streams.ingest.exchanges.bitfinex.model

import java.math.BigDecimal


data class BitfinexTickerRecordModel(
    val symbol: String,
    val bid: BigDecimal,
    val bidSize: BigDecimal,
    val ask: BigDecimal,
    val askSize: BigDecimal,
    val dailyChange: BigDecimal,
    val dailyChangeRelative: BigDecimal,
    val lastPrice: BigDecimal,
    val volume: BigDecimal,
    val high: BigDecimal,
    val low: BigDecimal
)