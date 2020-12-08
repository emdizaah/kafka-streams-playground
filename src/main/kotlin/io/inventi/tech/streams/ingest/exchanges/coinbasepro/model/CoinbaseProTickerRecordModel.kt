package io.inventi.tech.streams.ingest.exchanges.coinbasepro.model

import java.math.BigDecimal
import java.time.ZonedDateTime

data class CoinbaseProTickerRecordModel(
    val type: String,
    val sequence: Long,
    val product_id: String,
    val price: BigDecimal,
    val open_24h: BigDecimal,
    val volume_24h: BigDecimal,
    val low_24h: BigDecimal,
    val high_24h: BigDecimal,
    val volume_30d: BigDecimal,
    val best_bid: BigDecimal,
    val best_ask: BigDecimal,
    val side: String,
    val time: ZonedDateTime,
    val trade_id: Long,
    val last_size: BigDecimal
)
