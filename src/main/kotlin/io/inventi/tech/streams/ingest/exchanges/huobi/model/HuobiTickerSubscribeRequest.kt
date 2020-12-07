package io.inventi.tech.streams.ingest.exchanges.huobi.model

data class HuobiTickerSubscribeRequest(
    val sub: String,
    val id: String
) {
    companion object {
        fun btcusd(): HuobiTickerSubscribeRequest {
            return HuobiTickerSubscribeRequest("market.btcusdt.detail", "id1")
        }
    }
}