package com.example.port

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest
import io.micronaut.context.annotation.Value
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get


@Controller("/secret")
class SecretPort {

    @Value("\${aws.name}")
    lateinit var secretName: String

    @Value("\${aws.endpoint}")
    lateinit var endpoint: String

    @Value("\${aws.region}")
    lateinit var region: String

    //@Value("\${AWS_ACCESS_KEY}")
    //lateinit var key: String

    //@Value("\${AWS_SECRET_ID}")
    //lateinit var secret: String

    @Get
    fun secret(): String {

        val config = AwsClientBuilder.EndpointConfiguration(endpoint, region)

        val secretManager = AWSSecretsManagerClientBuilder.standard()
            .withEndpointConfiguration(config)
            //.withCredentials(AWSStaticCredentialsProvider(BasicAWSCredentials(key, secret)))
            .build()

        val getSecretValueRequest = GetSecretValueRequest()
            .withSecretId(secretName)

        val getSecretValueResponse = secretManager.getSecretValue(getSecretValueRequest);

        return getSecretValueResponse.secretString
    }

}