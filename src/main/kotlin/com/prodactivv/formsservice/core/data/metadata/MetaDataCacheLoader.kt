package com.prodactivv.formsservice.core.data.metadata

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.prodactivv.formsservice.communication.DataServiceBridgeService
import com.prodactivv.formsservice.communication.models.DataServiceModelField
import com.prodactivv.formsservice.communication.models.FormModel
import com.prodactivv.formsservice.config.RedisConfig
import org.springframework.stereotype.Service
import redis.clients.jedis.Jedis

@Service
class MetaDataCacheLoader(private val redisConfig: RedisConfig, private val objectMapper: ObjectMapper) {

    fun getModelsFromCache(): List<String>? {
        val jedis = Jedis(redisConfig.host, redisConfig.port.toInt())
        val cacheKey = "models"

        return if (jedis.exists(cacheKey)) {
            jedis.lrange(cacheKey, 0, -1).map { it }
        } else null
    }

    fun getModelDetailsFromCache(model: FormModel): List<DataServiceModelField>? {
        val jedis = Jedis(redisConfig.host, redisConfig.port.toInt())
        val cacheKey = model.toString()

        return if (jedis.exists(cacheKey)) {
            val cachedData = jedis.get(cacheKey)
            objectMapper.readValue(cachedData, object : TypeReference<List<DataServiceModelField>>() {})
        } else null
    }
}