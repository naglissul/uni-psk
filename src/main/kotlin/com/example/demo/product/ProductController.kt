package com.example.demo.product

import jakarta.persistence.OptimisticLockException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/product")
class ProductController(private val productRepository: ProductRepository, private val productService: ProductService) {

    @GetMapping
    fun getAllProducts(): List<Product> = productRepository.findAll()

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Long): ResponseEntity<Product> =
       ResponseEntity.ok().body(productService.getProductById(id))

    @PostMapping
    fun createProduct(@RequestBody product: Product): Product =
        productRepository.save(product)

    @PutMapping("/{id}")
    fun updateProduct(@PathVariable id: Long, @RequestBody updatedProduct: Product): ResponseEntity<Any> {

        val product = productService.updateProductTransaction(updatedProduct)

        //val product = productService.updateProductWithRetry(id, updatedProduct.name, updatedProduct.price)

        return ResponseEntity.ok().body(product)
    }

    @ExceptionHandler(OptimisticLockException::class)
    fun handleOptimisticLockException(e: OptimisticLockException): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.CONFLICT).body("Update conflict, please retry.")

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<Void> =
        productRepository.findById(id).map { product ->
            productRepository.delete(product)
            ResponseEntity<Void>(HttpStatus.NO_CONTENT)
        }.orElse(ResponseEntity.notFound().build())
}
