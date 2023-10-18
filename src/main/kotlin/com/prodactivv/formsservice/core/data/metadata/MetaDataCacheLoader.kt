package com.prodactivv.formsservice.core.data.metadata

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.prodactivv.formsservice.communication.data_service.api.data.DataServiceModelField
import com.prodactivv.formsservice.communication.data_service.api.data.FormModel
import org.springframework.stereotype.Service
import redis.clients.jedis.Jedis

@Service
class MetaDataCacheLoader(
    private val redis: Jedis,
    private val objectMapper: ObjectMapper
) {

    fun getModelsFromCache(): List<String>? {
        val cacheKey = "models"

        return if (redis.exists(cacheKey)) {
            redis.lrange(cacheKey, 0, -1).map { it }
        } else null
    }

    fun getModelDetailsFromCache(model: FormModel): List<DataServiceModelField>? {
        val cacheKey = model.toString()

        return if (redis.exists(cacheKey)) {
            val cachedData = redis.get(cacheKey)
            objectMapper.readValue(cachedData, object : TypeReference<List<DataServiceModelField>>() {})
        } else null
    }
}