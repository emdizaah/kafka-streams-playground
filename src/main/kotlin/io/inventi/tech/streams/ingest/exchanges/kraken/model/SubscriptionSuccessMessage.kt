package io.inventi.tech.streams.ingest.exchanges.kraken.model

data class SubscriptionSuccessMessage(
    val connectionID: String,
    val event: String,
    val status: String,
    val version: String
)