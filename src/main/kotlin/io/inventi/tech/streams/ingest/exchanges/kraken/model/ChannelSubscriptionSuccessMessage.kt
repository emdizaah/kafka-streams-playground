package io.inventi.tech.streams.ingest.exchanges.kraken.model

data class ChannelSubscriptionSuccessMessage(
    val channelID: String,
    val channelName: String,
    val event: String,
    val pair:  String,
    val status: String,
    val subscription: Subscription
)