package com.example.cartapi.domain

import com.example.cartapi.repository.CartItemRepository
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CartItemRepositoryTest(
    private val cartItemRepository: CartItemRepository
) {

    @Test
    fun testUpsert() {
        cartItemRepository.upsert(CartItem(1, 1, 1));
        cartItemRepository.upsert(CartItem(1, 1, 2));
        cartItemRepository.upsert(CartItem(1, 1, 3));

        cartItemRepository.upsert(CartItem(1, 1, 5));
        cartItemRepository.upsert(CartItem(1, 2, 30));
        cartItemRepository.upsert(CartItem(1, 3, 100));
    }

    @Test
    fun testBulkUpsert() {
        val cartItemList = listOf(
            CartItem(1, 1, 5),
            CartItem(1, 2, 20),
            CartItem(1, 3, 5),
            CartItem(2, 3, 5),
            CartItem(3, 3, 5),
            CartItem(3, 3, 100),
        )

        cartItemRepository.bulkUpsert(cartItemList)

        cartItemRepository.findAll().forEach { println("${it.userId}_${it.productId}_${it.quantity}") }
        val items = cartItemRepository.findByUserId(1)
        assertEquals(items.size, 3)
    }


}