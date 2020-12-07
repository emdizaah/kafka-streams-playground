package io.inventi.tech.streams.ingest.exchanges.binance.model

data class BinanceTickerSubscriptionMessage(
    val method: String,
    val params: List<String>,
    val id: Int
) {
    companion object {
        fun btcusd(): BinanceTickerSubscriptionMessage{
            return BinanceTickerSubscriptionMessage("SUBSCRIBE", listOf("btcusdt@ticker"), 42)
        }
    }
}