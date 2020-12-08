package io.inventi.tech.streams.ingest.exchanges.coinbasepro.model

data class CoinbaseProSubscribeRequest(
    val type: String,
    val channels: List<Channel>
) {
    companion object {
        fun btcusd(): CoinbaseProSubscribeRequest {
            return CoinbaseProSubscribeRequest("subscribe", listOf(Channel("ticker", listOf("BTC-USD"))))
        }
    }
}

data class Channel(val name: String, val product_ids: List<String>)