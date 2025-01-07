package com.example.cartapi.repository

import org.springframework.data.redis.core.Cursor
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ScanOptions
import org.springframework.stereotype.Component

@Component
class RedisRepository(
    private val redisTemplate: RedisTemplate<String, Any>
) {
    fun put(key: String, field: String, value: Int) {
        redisTemplate.opsForHash<String, Any>().put(key, field, value)
    }

    fun get(key: String, field: String): Int? {
        return redisTemplate.opsForHash<String, Int?>().get(key, field)
    }

    fun getAllByKey(key: String): MutableMap<String, Int> {
        return redisTemplate.opsForHash<String, Int?>().entries(key)
    }

    fun keys(pattern: String): Set<String> {
        return redisTemplate.keys(pattern)
    }

    fun getAllKeys(pattern: String): Set<String> {
        val keys = mutableSetOf<String>()
        val scanOptions: ScanOptions = ScanOptions.scanOptions().match(pattern).count(10).build()
        val connection = redisTemplate.connectionFactory?.connection ?: throw IllegalStateException()
        connection.scan(scanOptions).use { cursor ->
            cursor.asSequence().forEach { result ->
                keys.add(String(result))
            }
        }

        return keys
    }
}