package com.example.demo.config

import com.example.demo.product.ProductService
import com.example.demo.product.ProductServiceDecorator
import com.example.demo.product.ProductServiceDev
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class DecoratorConfig {

    @Value("\${decorator.enabled}")
    private var decoratorEnabled: Boolean = false

    @Bean
    @Primary
    fun productService(productServiceImpl: ProductServiceDev): ProductService {
        return if (decoratorEnabled) {
            ProductServiceDecorator(productServiceImpl)
        } else {
            productServiceImpl
        }
    }
}
