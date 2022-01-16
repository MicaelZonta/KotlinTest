package com.example.port

import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.Topic
import jakarta.inject.Singleton
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingDeque


@Singleton
@KafkaListener(groupId = "test", clientId = "test-consumer")
class TestListener {
    val messages: BlockingQueue<String> = LinkedBlockingDeque()

    @Topic("topic1")
    fun eventOccurred(body: String) {
        println("chegou")
        messages.add(body)
    }
}