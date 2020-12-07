package io.inventi.tech.streams.ingest.exchanges.binance.model

import com.fasterxml.jackson.annotation.JsonProperty


data class BinanceTickerRecordModel(
    @JsonProperty("e") val eventType: String,
    @JsonProperty("E") val eventTime: Long,
    @JsonProperty("s") val symbol: String,
    @JsonProperty("p") val priceChange: String,
    @JsonProperty("P") val priceChangePercent: String,
    @JsonProperty("w") val weightedAveragePrice: String,
    @JsonProperty("c") val lastPrice: String,
    @JsonProperty("Q") val lastQuantity: String,
    @JsonProperty("o") val openPrice: String,
    @JsonProperty("h") val highPrice: String,
    @JsonProperty("l") val lowPrice: String,
    @JsonProperty("v") val totalTradedBaseAssetVolume: String,
    @JsonProperty("q") val totalTradedQuoteAssetVolume: String,
    @JsonProperty("O") val statisticsOpenTime: Long,
    @JsonProperty("C") val statisticsCloseTime: Long,
    @JsonProperty("F") val firstTradeId: Long,
    @JsonProperty("L") val lastTradeId: Long,
    @JsonProperty("n") val totalNumberOfTrades: Long
)