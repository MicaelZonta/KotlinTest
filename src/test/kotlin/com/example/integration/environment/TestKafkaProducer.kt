package com.example.integration.testkafka

import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.Topic

@KafkaClient
interface TestProducer {
    @Topic("topic1")
    fun sendTestMessage(body: String?)
}