package com.example.demo.config
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before

@Aspect
class LoggingInterceptor {

    @Before("execution(* com.example.demo.product.ProductService.getProductById(..))")
    fun logBefore() {
        println("LoggingInterceptor: Before method getProductById execution")
    }
}