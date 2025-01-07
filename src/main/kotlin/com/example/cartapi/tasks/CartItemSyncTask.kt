package com.example.cartapi.tasks

import com.example.cartapi.domain.CartItem
import com.example.cartapi.repository.CartItemRepository
import com.example.cartapi.repository.RedisRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class CartItemSyncTask(
    private val redisRepository: RedisRepository,
    private val cartItemRepository: CartItemRepository
) {
    @Scheduled(cron = "*/3 * * * * *")
    fun syncByDB() {
        val keys = redisRepository.getAllKeys("user_cart:*")
        val items = mutableListOf<CartItem>()

        for (key in keys) {
            val cartItemByUser = redisRepository.getAllByKey(key)
            val userId = key.substring("user_cart:".length until key.length).toLong()
            items.addAll(cartItemByUser.entries.map { entry -> CartItem(userId, entry.key.toLong(), entry.value) })
        }

        cartItemRepository.bulkUpsert(items)
    }
}