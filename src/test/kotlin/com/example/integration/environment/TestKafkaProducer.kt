package com.example.integration.environment

import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.Topic

@KafkaClient
interface TestKafkaProducer {
    @Topic("topic1")
    fun sendTestMessage(body: String?)
}