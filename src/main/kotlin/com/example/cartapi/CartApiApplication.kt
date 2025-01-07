package com.example.cartapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CartApiApplication

fun main(args: Array<String>) {
    runApplication<CartApiApplication>(*args)
}
