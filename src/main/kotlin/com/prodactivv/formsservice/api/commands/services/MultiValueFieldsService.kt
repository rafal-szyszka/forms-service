package com.prodactivv.formsservice.api.commands.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.prodactivv.formsservice.api.commands.exceptions.DataNotFoundInRedis
import com.prodactivv.formsservice.api.commands.models.MultiValueFieldDto
import com.prodactivv.formsservice.communication.data_service.api.DataServiceBridgeService
import com.prodactivv.formsservice.communication.data_service.api.config.MultiValueFieldsConfig
import com.prodactivv.formsservice.core.data.models.Field
import com.prodactivv.formsservice.core.proql.models.ProQLQuery
import com.prodactivv.formsservice.redis.RedisComponent
import org.jobrunr.scheduling.BackgroundJob
import org.springframework.stereotype.Service
import java.security.MessageDigest

@Service
class MultiValueFieldsService(
    private val redisComponent: RedisComponent,
    private val dataServiceBridgeService: DataServiceBridgeService,
    private val multiValueFieldsConfig: MultiValueFieldsConfig
) {

    private val mapper = jacksonObjectMapper()

    fun fetchDataForField(field: Field): Field {
        val persistenceData = field.persistenceData
        val type = persistenceData!!.type
        if (!type.isPrimitive()) {
            val identifier = MessageDigest.getInstance("SHA-256")
                .digest(field.id!!.toByteArray(Charsets.UTF_8)).joinToString("")
            field.dataUrl = multiValueFieldsConfig.multiValueFieldsUrl + identifier
            var multiValueFieldData = MultiValueFieldDto(getFieldProQLQuery(persistenceData.typeString()))
            redisComponent.redis().setex(
                identifier,
                multiValueFieldsConfig.multiValueFieldsTTL.toLong(),
                mapper.writeValueAsString(multiValueFieldData)
            )
            BackgroundJob.enqueue { getDataForRedis(identifier) }
        }
        return field
    }

    fun getData(id: String): Any {
        val dataFromRedis = redisComponent.redis().get(id)
        if (dataFromRedis.isEmpty()) {
            throw DataNotFoundInRedis()
        }
        val dataMappedToObject: MultiValueFieldDto = mapper.readValue(dataFromRedis)
        if (dataMappedToObject.result.isNullOrEmpty()) {
            return mapper.writeValueAsString(dataServiceBridgeService.getData(dataMappedToObject.proQLQuery))
        }
        return dataMappedToObject.result!!
    }

    fun getDataForRedis(identifier: String) {
        val dataMappedToObject: MultiValueFieldDto = mapper.readValue(redisComponent.redis().get(identifier))
        if (dataMappedToObject.result.isNullOrEmpty()) {
            dataMappedToObject.result = dataServiceBridgeService.getData(dataMappedToObject.proQLQuery)
            redisComponent.redis().setex(
                identifier,
                multiValueFieldsConfig.multiValueFieldsTTL.toLong(),
                mapper.writeValueAsString(dataMappedToObject)
            )
        }
    }

    private fun getFieldProQLQuery(persistenceData: String): ProQLQuery {
        return ProQLQuery(
            persistenceData,
            null,
            null
        )
    }
}