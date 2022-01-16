package com.example.port

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/hello")
class HelloPort {

    @Get
    fun hello(): String {
        return "Hello World"
    }

}