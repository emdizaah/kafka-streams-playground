package io.inventi.tech.streams.parser

import io.inventi.tech.streams.model.BookRecordUpdateIncoming
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

@Component
class CryptoDataParser(private val kafkaObjectMapper: ObjectMapper) {

    fun parseToBookEvent(v: String): BookRecordUpdateIncoming? {
        return parseToBookRecordUpdate(v)
    }

    fun parseToBookRecordUpdate(v: String): BookRecordUpdateIncoming? {
        try {
            val jsonNode = kafkaObjectMapper.readTree(v)
            val channelId = jsonNode[0].intValue()
            var ask: BookRecordUpdateIncoming.BookTypeAsk? = null
            var buy: BookRecordUpdateIncoming.BookTypeBuy? = null
            if (jsonNode[1].hasNonNull("a")) {
                ask = kafkaObjectMapper.readValue(jsonNode[1].toString(), BookRecordUpdateIncoming.BookTypeAsk::class.java)
                if (jsonNode[2].hasNonNull("b")) {
                    buy = kafkaObjectMapper.readValue(jsonNode[2].toString(), BookRecordUpdateIncoming.BookTypeBuy::class.java)
                }
            } else if (jsonNode[1].hasNonNull("b")) {
                buy = kafkaObjectMapper.readValue(jsonNode[1].toString(), BookRecordUpdateIncoming.BookTypeBuy::class.java)
                if (jsonNode[2].hasNonNull("a")) {
                    ask = kafkaObjectMapper.readValue(jsonNode[2].toString(), BookRecordUpdateIncoming.BookTypeAsk::class.java)
                }
            } else return null

            return if (jsonNode.size() == 4) {
                val bookRecord = BookRecordUpdateIncoming(channelId, ask, buy, jsonNode[2].textValue(), jsonNode[3].textValue())
                bookRecord
            } else {
                val bookRecord = BookRecordUpdateIncoming(channelId, ask, buy, jsonNode[3].textValue(), jsonNode[4].textValue())
                bookRecord
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            return null
        }
    }


}
