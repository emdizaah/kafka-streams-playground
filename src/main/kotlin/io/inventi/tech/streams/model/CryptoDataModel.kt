package io.inventi.tech.streams.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal

@JsonFormat(shape= JsonFormat.Shape.ARRAY)
data class BookRecordUpdateIncoming(
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

@JsonInclude(JsonInclude.Include.NON_NULL)
data class BookRecordUpdate(
    val channelId: Int,
    val ask: BookTypeAsk? = null,
    val buy: BookTypeBuy? = null,
    val channelName: String,
    val pair: String

) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class Book(
        val price: BigDecimal,
        val volume: BigDecimal,
        val timestamp: BigDecimal,
        val updateType: String? = null
    )

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class BookTypeAsk(
        val a: List<Book>?,
        val c: String? = null
    )

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class BookTypeBuy(
        val b: List<Book>?,
        val c: String? = null
    )

}

fun fromIncoming(incoming: BookRecordUpdateIncoming): BookRecordUpdate {
    return BookRecordUpdate(
        channelId = incoming.channelId,
        pair = incoming.pair,
        channelName = incoming.channelName,

        ask = BookRecordUpdate.BookTypeAsk(
            a = incoming.ask?.a?.map { BookRecordUpdate.Book(price = it.price, volume = it.price, timestamp = it.timestamp, updateType = it.updateType) },
            c = incoming.ask?.c
        ),

        buy = BookRecordUpdate.BookTypeBuy(
            b = incoming.buy?.b?.map { BookRecordUpdate.Book(price = it.price, volume = it.price, timestamp = it.timestamp, updateType = it.updateType) },
            c = incoming.buy?.c
        )
    )
}
