import java.math.BigDecimal

data class KrakenTickerRecordModel(
        val channelId: Int,
        val ask: Ask,
        val buy: Buy,
        val close: Close,
        val volume: Volume,
        val price: VolumeWeightedAveragePrice,
        val trades: NumberOfTrades,
        val low: LowPrice,
        val high: HighPrice,
        val open: OpenPrice,
        val pair: String //TODO change to common enum
)

data class Ask(val price: BigDecimal, val wholeLotVolume: Int, val lotVolume: BigDecimal)

data class Buy(val price: BigDecimal, val wholeLotVolume: Int, val lotVolume: BigDecimal)

data class Close(val price: BigDecimal, val lotVolume: BigDecimal)

data class Volume(val valueToday: BigDecimal, val last24Hours: BigDecimal)

data class VolumeWeightedAveragePrice(val valueToday: BigDecimal, val last24Hours: BigDecimal)

data class NumberOfTrades(val today: Int, val last24Hours: Int)

data class LowPrice(val today: BigDecimal, val last24Hours: BigDecimal)

data class HighPrice(val today: BigDecimal, val last24Hours: BigDecimal)

data class OpenPrice(val today: BigDecimal, val last24Hours: BigDecimal)



