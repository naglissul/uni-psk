package com.example.demo.product
import jakarta.persistence.EntityManager
import jakarta.persistence.OptimisticLockException
import jakarta.persistence.PersistenceContext
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Profile("dev")
//@Primary
class ProductServiceDev(private val productRepository: ProductRepository, private val longComputationComponent: LongComputationComponent): ProductService{

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @Transactional
    override fun updateProductTransaction(updatedProduct: Product): Product {
        val product = productRepository.findById(updatedProduct.id).orElseThrow { throw IllegalArgumentException("Product not found") }

        product.name = updatedProduct.name
        productRepository.save(product)
        entityManager.flush()

        try {
            Thread.sleep(5000)
        } catch (e: InterruptedException) {
            System.out.println("Sleep interrupted")
            e.printStackTrace()
        }

        product.price = updatedProduct.price
        productRepository.save(product)
        entityManager.flush()

        //throw IllegalStateException("This is failing transaction")

        return product;
    }

    @Transactional
    override fun updateProductWithRetry(id: Long, newName: String, newPrice: Double) : Product {
        var attempts = 0
        val maxAttempts = 3

        while (attempts < maxAttempts) {
            try {
                val product = productRepository.findById(id).orElseThrow { RuntimeException("Product not found") }
                product.name = newName
                productRepository.save(product)
                entityManager.flush()

                try {
                    Thread.sleep(5000)
                } catch (e: InterruptedException) {
                    System.out.println("Sleep interrupted")
                    e.printStackTrace()
                }

                product.price = newPrice
                productRepository.save(product)
                entityManager.flush()
                return product
            } catch (e: OptimisticLockException) {
                attempts++
                if (attempts >= maxAttempts) {
                    throw RuntimeException("Failed to update product due to concurrent modification after $attempts attempts", e)
                }
            }
        }
        // This should never be reached
        return Product(
            name = newName,
            price = newPrice
        )
    }

//    @Transactional
//    fun getProductById(id: Long): Product {
//        return productRepository.findById(id).orElseThrow { throw IllegalArgumentException("Product not found") }
//    }

    @Transactional
    override fun getProductById(id: Long): Product {
        val product = productRepository.findById(id).orElseThrow { throw IllegalArgumentException("Product not found") }

        // async op
        val future = longComputationComponent.performLongComputation(product.price.toInt())
        Thread.sleep(5000) // Simulate some processing time
        product.price = future.get().toDouble()
        System.out.println("WE ARE IN DEV")
        return product
    }

}
