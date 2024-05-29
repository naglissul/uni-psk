package com.example.demo.product

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

@Service
class ProductServiceDecorator(
    @Qualifier("productServiceDev") private val delegate: ProductService
) : ProductService {
    override fun updateProductTransaction(updatedProduct: Product): Product {
        System.out.println("ProductServiceDecorator: updateProductTransaction called")
        return delegate.updateProductTransaction(updatedProduct)
    }

    override fun updateProductWithRetry(id: Long, newName: String, newPrice: Double): Product {
        System.out.println("ProductServiceDecorator: updateProductWithRetry called")
        return delegate.updateProductWithRetry(id, newName, newPrice)
    }

    override fun getProductById(id: Long): Product {
        System.out.println("ProductServiceDecorator: getProductById called")
        return delegate.getProductById(id)
    }
}
