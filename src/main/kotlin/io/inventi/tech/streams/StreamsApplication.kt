package io.inventi.tech.streams

import com.fasterxml.jackson.databind.ObjectMapper
import io.inventi.tech.streams.model.BookRecordUpdate
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.kstream.Consumed
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

        val props = mapOf(
            StreamsConfig.APPLICATION_ID_CONFIG to "stream-app",
            StreamsConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092"
        ).toProperties()

        val streamsBuilder = StreamsBuilder()

        val consumedWith = Consumed.with(
            Serdes.String(),
            Serdes.serdeFrom(
                JsonSerializer<BookRecordUpdate>(kafkaObjectMapper),
                JsonDeserializer<BookRecordUpdate>(kafkaObjectMapper)
                    .apply { addTrustedPackages("*") }))

        val cryptoStream = streamsBuilder
            .stream<String, BookRecordUpdate>("crypto", consumedWith)

        val kafkaStreams = KafkaStreams(streamsBuilder.build(), props)

        kafkaStreams.start()
    }

}
