package com.example.demo

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.kstream.KStream
import org.springframework.beans.factory.FactoryBean
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.kafka.annotation.EnableKafkaStreams
import org.springframework.kafka.config.KafkaStreamsConfiguration
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

@Configuration
class KafkaStreamsConfig {
    @Bean
    fun kafkaObjectMapper(): ObjectMapper {
        return ObjectMapper()
            .registerModule(KotlinModule())
            .registerModule(JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }

    @Bean
    fun producerFactory(
        properties: KafkaProperties,
        kafkaObjectMapper: ObjectMapper
    ) : ProducerFactory<String, String> =
        DefaultKafkaProducerFactory(
            properties.buildProducerProperties(),
            StringSerializer(),
            StringSerializer()
        )

    @Bean
    fun cryptoDataProducerTemplate( factory: ProducerFactory<String, String>): KafkaTemplate<String, String> =
        KafkaTemplate(factory)
}
