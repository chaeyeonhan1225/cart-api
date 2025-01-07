package com.example.cartapi.service

import com.example.cartapi.repository.CartItemRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.test.context.TestConstructor
import kotlin.test.Test

@SpringBootTest
@EnableScheduling
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CartItemServiceTest(
    private val cartItemService: CartItemService,
    private val cartItemRepository: CartItemRepository
) {
    @Test
    fun 유저가_여러번_값을_수정() {
        val requestCount = 100

        for (quantity in 1..requestCount) {
            cartItemService.setCartItem(1, 3, quantity = quantity)
        }

        val cartItems = cartItemRepository.findByUserId(1)
        assertEquals(cartItems.first().quantity, 100)
    }

    @Test
    fun 유저가_여러번_값을_수정_Redis() {
        val requestCount = 20

        for (quantity in 1..requestCount) {
            cartItemService.setCartItemCache(2, 3, quantity = quantity)
            cartItemService.setCartItemCache(2, 10, quantity = quantity)
            cartItemService.setCartItemCache(1, 60, quantity = quantity)
            cartItemService.setCartItemCache(3, 3, quantity = quantity)
            cartItemService.setCartItemCache(4, 10, quantity = quantity)
            cartItemService.setCartItemCache(5, 60, quantity = quantity)
        }


        val cartItemsByUser = cartItemService.getCartItemsByUser(2)
        assertEquals(cartItemsByUser.first().quantity, requestCount)
        val cartItemsBeforeSync = cartItemRepository.findByUserId(2)

        assertEquals(cartItemsBeforeSync.size, 0)
        Thread.sleep(5000)

        val cartItemsAfterSync = cartItemRepository.findByUserId(1)
        assertEquals(cartItemsAfterSync.first().quantity, requestCount)
    }
}