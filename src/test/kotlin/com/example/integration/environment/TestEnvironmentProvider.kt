package com.example.integration.environment


import com.amazonaws.auth.SystemPropertiesCredentialsProvider
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder
import com.amazonaws.services.secretsmanager.model.CreateSecretRequest
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.junit.jupiter.api.TestInstance
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap
import org.testcontainers.utility.DockerImageName


@Testcontainers
@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class TestEnvironmentProvider : TestPropertyProvider {

    var kafka = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1")).apply { start() }

    var localstackImage: LocalStackContainer =
        (LocalStackContainer(DockerImageName.parse("localstack/localstack:0.11.3")).withServices(
            LocalStackContainer.Service.S3,
            LocalStackContainer.Service.SECRETSMANAGER
        )).apply { start() }


    override fun getProperties(): MutableMap<String, String> {

        this.createSecret()
        this.createNewS3()
        this.createNewTopic()

        return mutableMapOf(
            "KAFKA_URL" to kafka.bootstrapServers,
            "AWS_NAME" to "secretName",
            "AWS_ENDPOINT" to localstackImage.getEndpointConfiguration(LocalStackContainer.Service.SECRETSMANAGER).serviceEndpoint,
            "AWS_REGION" to localstackImage.getEndpointConfiguration(LocalStackContainer.Service.SECRETSMANAGER).signingRegion,
            "cloud.aws.region.static" to localstackImage.getEndpointConfiguration(LocalStackContainer.Service.SECRETSMANAGER).signingRegion,
            "cloud.aws.credentials.access-key" to localstackImage.defaultCredentialsProvider.credentials.awsAccessKeyId,
            "cloud.aws.credentials.secret-key" to localstackImage.defaultCredentialsProvider.credentials.awsSecretKey,
            "AWS_ACCESS_ID" to localstackImage.defaultCredentialsProvider.credentials.awsAccessKeyId,
            "AWS_SECRET_ID" to localstackImage.defaultCredentialsProvider.credentials.awsSecretKey
        )
    }

    private fun createNewS3() {
        val s3: AmazonS3 = AmazonS3ClientBuilder
            .standard()
            .withEndpointConfiguration(localstackImage.getEndpointConfiguration(LocalStackContainer.Service.S3))
            .withCredentials(localstackImage.defaultCredentialsProvider)
            .build()

        s3.createBucket("foo")
        s3.putObject("foo", "bar", "baz")
    }

    private fun createSecret(): String {

        val secretManager = AWSSecretsManagerClientBuilder.standard()
            .withEndpointConfiguration(localstackImage.getEndpointConfiguration(LocalStackContainer.Service.S3))
            .withCredentials(localstackImage.defaultCredentialsProvider)
            .build()

        val secretRequest = CreateSecretRequest()
            .withName("secretName")
            .withDescription("This secret was created by the AWS Secret Manager Java API")
            .withSecretString("secretValue")

        val secretResponse = secretManager.createSecret(secretRequest)

        return secretResponse.arn
    }

    private fun createNewTopic() {
        val adminClient = AdminClient.create(
            ImmutableMap.of<String, Any>(
                AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.bootstrapServers
            )
        )
        var novoTopico = NewTopic("topic1", 1, 1)

        adminClient.createTopics(listOf(novoTopico))
    }
}