package com.example.demo

import com.example.demo.model.BookRecordUpdate
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.Predicate
import org.apache.kafka.streams.kstream.Produced
import org.springframework.beans.factory.InitializingBean
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer
import org.springframework.stereotype.Component


@Component
class StreamsApplication(private val kafkaObjectMapper: ObjectMapper) : InitializingBean {

    override fun afterPropertiesSet() {
        launch()
    }

    fun launch() {
        val streamsBuilder = StreamsBuilder()

        val cryptoStream = streamsBuilder
            .stream<String, BookRecordUpdate>(
                "crypto",
                Consumed.with(
                    Serdes.String(),
                    Serdes.serdeFrom(
                        JsonSerializer<BookRecordUpdate>(kafkaObjectMapper),
                        JsonDeserializer<BookRecordUpdate>(kafkaObjectMapper)
                            .apply { addTrustedPackages("*") })))

        val cryptoStreams = cryptoStream
            .filterNot { _, v -> v.buy == null }
            .branch(
                Predicate { _, v -> v.pair == "XBT/USD"},
                Predicate { _, v -> v.pair == "ETH/USD"},
                Predicate { _, v -> v.pair == "XRP/USD"},
                Predicate { _, v -> v.pair == "BCH/USD"}
            )



        val btcStream = cryptoStreams[0].mapValues { _, v -> v.pair }.to("xbt", Produced.with(Serdes.String(), Serdes.String()))
        val ethStream = cryptoStreams[1].mapValues { _, v -> v.pair}.to("eth", Produced.with(Serdes.String(), Serdes.String()))
        val rippleStream = cryptoStreams[2].mapValues { _, v -> v.pair}.to("xrp", Produced.with(Serdes.String(), Serdes.String()))
        val btcCashStream = cryptoStreams[3].mapValues { _, v -> v.pair}.to("bch", Produced.with(Serdes.String(), Serdes.String()))



        val topology: Topology = streamsBuilder.build()
        val kafkaStreams = KafkaStreams(topology, mapOf(
            StreamsConfig.APPLICATION_ID_CONFIG to "stream-app",
            StreamsConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
            JsonDeserializer.TRUSTED_PACKAGES to "*"
        ).toProperties())

        kafkaStreams.start()
    }

}
