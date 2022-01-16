package com.example.advice

import io.micronaut.aop.Around
import io.micronaut.context.annotation.Type

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Around
@Type(KafkaInterceptor::class)
annotation class KafkaInterceptorAdvice {


}