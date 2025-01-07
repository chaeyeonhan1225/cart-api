package com.example.cartapi.domain

class CartItem(
    val userId: Long,
    val productId: Long,
    quantity: Int
) {
   var quantity: Int = quantity
    private set

    fun updateQuantity(quantity: Int) {
        this.quantity = quantity
    }
}