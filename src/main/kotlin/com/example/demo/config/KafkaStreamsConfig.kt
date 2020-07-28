package com.example.demo.config

import com.example.demo.model.BookRecordUpdate
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
class KafkaStreamsConfig {
    @Bean
    fun kafkaObjectMapper(): ObjectMapper {
        return ObjectMapper()
            .registerModule(KotlinModule())
            .registerModule(JavaTimeModule())
            .configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }

    @Bean
    fun producerFactory(
        properties: KafkaProperties,
        kafkaObjectMapper: ObjectMapper
    ) : ProducerFactory<String, BookRecordUpdate> =
        DefaultKafkaProducerFactory(
            properties.buildProducerProperties(),
            StringSerializer(),
            JsonSerializer<BookRecordUpdate>()
        )

    @Bean
    fun cryptoDataProducerTemplate( factory: ProducerFactory<String, BookRecordUpdate>): KafkaTemplate<String, BookRecordUpdate> =
        KafkaTemplate(factory)
}
