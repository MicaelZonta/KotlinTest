package com.example.advice

import io.micronaut.aop.InterceptorBean
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import io.micronaut.context.annotation.Prototype

@Prototype
@InterceptorBean(KafkaListenerUserContext::class)
class KafkaListenerUserContextInterceptor : MethodInterceptor<Any, Any> {

    override fun intercept(context: MethodInvocationContext<Any, Any>?): Any? {


        return context?.proceed()
    }


}