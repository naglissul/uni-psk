package com.example.demo.product

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class LongComputationComponent {

    @Async
    fun performLongComputation(input: Int): CompletableFuture<Int> {
        try {
            // Simulate long computation
            Thread.sleep(5000)  // 5 seconds delay
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
            throw RuntimeException(e)
        }
        val result = input * 2  // Example computation
        return CompletableFuture.completedFuture(result)
    }
}
