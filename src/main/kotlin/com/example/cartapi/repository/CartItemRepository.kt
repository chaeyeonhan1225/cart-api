package com.example.cartapi.repository

import com.example.cartapi.domain.CartItem
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.PreparedStatement
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class CartItemRowMapper : RowMapper<CartItem> {
    override fun mapRow(rs: ResultSet, rowNum: Int): CartItem {
        return CartItem(
            userId = rs.getLong("userId"),
            productId = rs.getLong("productId"),
            quantity = rs.getInt("quantity")
        )
    }
}

@Repository
class CartItemRepository(
    private val jdbcTemplate: JdbcTemplate
) {
    fun upsert(cartItem: CartItem) {
        val sql = """INSERT INTO CartItem (userId, productId, quantity) VALUES (?, ?, ?)
             ON DUPLICATE KEY UPDATE quantity = VALUES(quantity)"""

        jdbcTemplate.update(sql, cartItem.userId, cartItem.productId, cartItem.quantity)
    }

    fun bulkUpsert(cartItems: List<CartItem>) {
        val sql = "INSERT INTO CartItem (userId, productId, quantity) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE quantity = VALUES(quantity)"
        val batchPreparedStatementSetter = object : BatchPreparedStatementSetter {
            override fun setValues(ps: PreparedStatement, i: Int) {
                val item = cartItems[i]
                ps.setLong(1, item.userId)
                ps.setLong(2, item.productId)
                ps.setInt(3, item.quantity)
            }

            override fun getBatchSize(): Int = cartItems.size

        }
        jdbcTemplate.batchUpdate(sql, batchPreparedStatementSetter)
    }

    fun findAll(): List<CartItem> {
        val sql = "SELECT * FROM cartitem"
        return jdbcTemplate.query(sql, CartItemRowMapper())
    }

    fun findByUserId(userId: Long): List<CartItem> {
        val sql = "SELECT * FROM cartitem WHERE userId = ?"
        return jdbcTemplate.query(sql, CartItemRowMapper(), userId)
    }
}