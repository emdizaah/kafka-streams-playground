package com.example.demo.parser

import com.example.demo.model.BookRecordUpdate
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

@Component
class CryptoDataParser(private val kafkaObjectMapper: ObjectMapper) {

    fun parseToBookEvent(v: String): BookRecordUpdate? {
        return parseToBookRecordUpdate(v)
    }

    fun parseToBookRecordUpdate(v: String): BookRecordUpdate? {
        try {
            val jsonNode = kafkaObjectMapper.readTree(v)
            val channelId = jsonNode[0].intValue()
            var ask: BookRecordUpdate.BookTypeAsk? = null
            var buy: BookRecordUpdate.BookTypeBuy? = null
            if (jsonNode[1].hasNonNull("a")) {
                ask = kafkaObjectMapper.readValue(jsonNode[1].toString(), BookRecordUpdate.BookTypeAsk::class.java)
                if (jsonNode[2].hasNonNull("b")) {
                    buy = kafkaObjectMapper.readValue(jsonNode[2].toString(), BookRecordUpdate.BookTypeBuy::class.java)
                }
            } else if (jsonNode[1].hasNonNull("b")) {
                buy = kafkaObjectMapper.readValue(jsonNode[1].toString(), BookRecordUpdate.BookTypeBuy::class.java)
                if (jsonNode[2].hasNonNull("a")) {
                    ask = kafkaObjectMapper.readValue(jsonNode[2].toString(), BookRecordUpdate.BookTypeAsk::class.java)
                }
            } else return null

            return if (jsonNode.size() == 4) {
                val bookRecord = BookRecordUpdate(channelId, ask, buy, jsonNode[2].textValue(), jsonNode[3].textValue())
                bookRecord
            } else {
                val bookRecord = BookRecordUpdate(channelId, ask, buy, jsonNode[3].textValue(), jsonNode[4].textValue())
                bookRecord
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            return null
        }
    }


}
