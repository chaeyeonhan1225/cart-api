package com.example.cartapi.service

import com.example.cartapi.domain.CartItem
import com.example.cartapi.repository.CartItemRepository
import com.example.cartapi.repository.RedisRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CartItemService(
    private val cartItemRepository: CartItemRepository,
    private val redisRepository: RedisRepository
) {
    @Transactional
    fun setCartItem(userId: Long, productId: Long, quantity: Int) {
        cartItemRepository.upsert(CartItem(userId, productId, quantity));
    }

    fun setCartItemCache(userId: Long, productId: Long, quantity: Int) {
        redisRepository.put(generateKey(userId), productId.toString(), quantity);
    }

    fun getCartItemsByUser(userId: Long): List<CartItem> {
        val cachedCartItem = redisRepository.getAllByKey(generateKey(userId));
        if (cachedCartItem.isNotEmpty()) {
            println(cachedCartItem.entries)
            return cachedCartItem.entries.map { CartItem(userId, it.key.toLong(), it.value) }
        }

        return cartItemRepository.findByUserId(userId)
    }

    private fun generateKey(userId: Long): String {
        return "user_cart:${userId}"
    }
}