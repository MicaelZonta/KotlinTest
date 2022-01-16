package com.example.integration

import com.example.integration.environment.TestEnvironmentProvider
import com.example.integration.environment.TestKafkaProducer
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.testcontainers.junit.jupiter.Testcontainers

@MicronautTest
open class KafkaTest : TestEnvironmentProvider() {

    @Inject
    lateinit var testKafkaProducer: TestKafkaProducer

    @Test
    fun `ouvirMensagem`() {
        testKafkaProducer.sendTestMessage("teste")
        testKafkaProducer.sendTestMessage("teste")
    }

}