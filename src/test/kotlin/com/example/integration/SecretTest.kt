package com.example.integration

import com.example.integration.environment.TestEnvironmentProvider
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Test

@MicronautTest
class HttpTest : TestEnvironmentProvider() {

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Test
    fun `should return Hello World message`() {
        val request: HttpRequest<Any> = HttpRequest.GET("/hello")
        val body = httpClient.toBlocking().retrieve(request)

        println(body.toString())
    }

}
