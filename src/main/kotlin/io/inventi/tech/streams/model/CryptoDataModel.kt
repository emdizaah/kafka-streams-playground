package io.inventi.tech.streams.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.math.BigDecimal

@JsonFormat(shape= JsonFormat.Shape.ARRAY)
data class BookRecordUpdate(
    val channelId: Int,
    val ask: BookTypeAsk? = null,
    val buy: BookTypeBuy? = null,
    val channelName: String,
    val pair: String
) {
    @JsonFormat(shape= JsonFormat.Shape.ARRAY)
    data class Book(
        val price: BigDecimal,
        val volume: BigDecimal,
        val timestamp: BigDecimal,
        val updateType: String? = null
    )

    data class BookTypeAsk(
        val a: List<Book>,
        val c: String? = null
    )

    data class BookTypeBuy(
        val b: List<Book>,
        val c: String? = null
    )
}
