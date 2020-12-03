package io.inventi.tech.streams.ingest.exchanges.bitfinex.model

data class BitfinexTickerSubscribeRequest(
        val event: String,
        val channel: String,
        val symbol: String
) {
    companion object {
        fun btcusd(): BitfinexTickerSubscribeRequest {
            return BitfinexTickerSubscribeRequest("subscribe", "Ticker", "tBTCUSD")
        }
    }
}