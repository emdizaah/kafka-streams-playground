package io.inventi.tech.streams.ingest.exchanges.binance.model

data class BinanceTickerSubscribeRequest(
    val method: String,
    val params: List<String>,
    val id: Int = 1
) {
    companion object {
        fun btcusd(): BinanceTickerSubscribeRequest {
            return BinanceTickerSubscribeRequest("SUBSCRIBE", listOf("btcusd@aggTrade"), 42)
        }
    }
}