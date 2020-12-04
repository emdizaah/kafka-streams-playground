package io.inventi.tech.streams.ingest.exchanges.binance.parser

import io.inventi.tech.streams.ingest.exchanges.binance.model.BinanceTickerRecordModel
import io.inventi.tech.streams.ingest.exchanges.bitfinex.parser.BitfinexMessageParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


@Component
class BinanceMessageParser {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(BinanceMessageParser::class.java)
    }

    fun parseToTicker(payload: String): BinanceTickerRecordModel? {
        logger.info("Message from BINANCE!!!")
        logger.info(payload)
        return null
    }
}