package com.example.demo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.Produced
import org.springframework.beans.factory.InitializingBean
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer
import org.springframework.stereotype.Component
import java.lang.System.getProperties


@Component
class StreamsApplication(private val kafkaObjectMapper: ObjectMapper) : InitializingBean {

    override fun afterPropertiesSet() {
        launch()
    }

    fun launch() {
        val streamsConfig = StreamsConfig(mapOf(
            StreamsConfig.APPLICATION_ID_CONFIG to "stream-app",
            StreamsConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092"
        ))

        val streamsBuilder = StreamsBuilder()

        val purchaseStream = streamsBuilder.stream<String, String>("crypto", Consumed.with(Serdes.String(), Serdes.String()))

        purchaseStream
            .mapValues { v -> kafkaObjectMapper.readValue(v, CryptoExchangeMessage::class.java).type }
            .to("types", Produced.with(Serdes.String(), Serdes.String()))

        val kafkaStreams = KafkaStreams(streamsBuilder.build(), streamsConfig)

        kafkaStreams.start()
    }

}

@JsonIgnoreProperties(ignoreUnknown = true)
data class CryptoExchangeMessage(val type: String)
