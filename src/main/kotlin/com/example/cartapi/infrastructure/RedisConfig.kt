package com.example.cartapi.infrastructure

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer


@Configuration
class RedisConfig {
    @Bean
    fun redisTemplate(connectionFactory: RedisConnectionFactory?): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.connectionFactory = connectionFactory

        template.keySerializer = StringRedisSerializer()

        template.valueSerializer = GenericJackson2JsonRedisSerializer()

        template.hashKeySerializer = StringRedisSerializer()

        template.hashValueSerializer = GenericJackson2JsonRedisSerializer()

        template.afterPropertiesSet()
        return template
    }
}