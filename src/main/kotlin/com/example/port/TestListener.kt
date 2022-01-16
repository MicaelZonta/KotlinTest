package com.example.port

import com.example.advice.KafkaListenerUserContext
import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.Topic
import jakarta.inject.Singleton
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingDeque

@Singleton
@KafkaListener(groupId = "test", clientId = "test-consumer")
open class TestListener {
    val messages: BlockingQueue<String> = LinkedBlockingDeque()

    @Topic("topic1")
    @KafkaListenerUserContext
    open fun eventOccurred(body: String) {
        println("chegou")
        messages.add(body)
    }
}