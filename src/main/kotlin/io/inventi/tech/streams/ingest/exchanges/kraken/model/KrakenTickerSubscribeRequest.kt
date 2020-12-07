package io.inventi.tech.streams.ingest.exchanges.kraken.model

data class KrakenTickerSubscribeRequest(
        val event: String,
        val pair: List<String>,
        val subscription: Subscription
) {
    companion object {
        fun btcusd(): KrakenTickerSubscribeRequest {
            return KrakenTickerSubscribeRequest("subscribe", listOf("XBT/USD"), Subscription("ticker"))
        }
    }
}