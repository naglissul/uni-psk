package com.example.demo.product

interface ProductService {
    fun updateProductTransaction(updatedProduct: Product): Product
    fun updateProductWithRetry(id: Long, newName: String, newPrice: Double) : Product
    fun getProductById(id: Long): Product
}