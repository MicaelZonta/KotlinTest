package com.example.integration.enviromentprovider

import com.example.integration.testkafka.TestProducer
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap
import org.testcontainers.utility.DockerImageName


@Testcontainers
@MicronautTest()
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class TestEnviromentProvider : TestPropertyProvider {

    var kafka = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1")).apply { start() }

    override fun getProperties(): MutableMap<String, String> {

        val adminClient = AdminClient.create(
            ImmutableMap.of<String, Any>(
                AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.bootstrapServers
            )
        )
        var novoTopico = NewTopic("topic1", 1, 1)

        adminClient.createTopics(listOf(novoTopico))

        return mutableMapOf(
            "KAFKA_URL" to kafka.bootstrapServers
        )
    }
}