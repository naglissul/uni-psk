package com.example.demo.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy

@Configuration
@EnableAspectJAutoProxy
class InterceptorConfig {

    @ConditionalOnProperty(name = ["logging.interceptor.enabled"], havingValue = "true")
    @Bean
    fun loggingInterceptor(): LoggingInterceptor? {
        return LoggingInterceptor()
    }
}
