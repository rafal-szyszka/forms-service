package com.prodactivv.formsservice.communication.data_service.api

import com.prodactivv.formsservice.communication.data_service.api.data.DataServiceModelField
import com.prodactivv.formsservice.communication.data_service.api.data.FormModel
import com.prodactivv.formsservice.communication.data_service.api.data.PRIMITIVES
import com.prodactivv.formsservice.communication.data_service.api.exceptions.UnsupportedFormModelType
import com.prodactivv.formsservice.core.data.metadata.MetaDataCacheLoader
import com.prodactivv.formsservice.core.proql.models.ProQLCommand
import com.prodactivv.formsservice.core.proql.models.ProQLQuery
import org.springframework.stereotype.Service

@Service
class DataServiceBridgeService(
    private val restDataServiceCalls: RestDataServiceCalls,
    private val metaDataCacheLoader: MetaDataCacheLoader
) {

    fun getData(proQLQuery: ProQLQuery): List<Map<String, Any>>  {
        return restDataServiceCalls.getData(proQLQuery)
    }

    fun getModels(): List<FormModel> {
        val cachedModels = metaDataCacheLoader.getModelsFromCache()
        if (!cachedModels.isNullOrEmpty()) {
            return cachedModels.map {
                val formModel = it.split(".")
                FormModel(formModel[0], formModel[1])
            }
        }

        return restDataServiceCalls.getModels().map {
            val formModel = it.split(".")
            FormModel(formModel[0], formModel[1])
        }
    }

    fun getModels(module: String): List<FormModel> {
        return restDataServiceCalls.getModels(module).map {
            val formModel = it.split(".")
            FormModel(formModel[0], formModel[1])
        }
    }

    fun getModel(module: String, model: String): List<DataServiceModelField> {
        val modelToFetch = FormModel(module, model)
        val cachedModelDetails = metaDataCacheLoader.getModelDetailsFromCache(modelToFetch)

        return when (cachedModelDetails.isNullOrEmpty()) {
            true -> restDataServiceCalls.getModel(modelToFetch)
            else -> cachedModelDetails
        }.map {
            val formModel = it.type.split(".")
            DataServiceModelField(
                it.name,
                when (formModel.size) {
                    1 -> FormModel(PRIMITIVES, it.type).toString()
                    2 -> FormModel(formModel[0], formModel[1]).toString()
                    else -> throw UnsupportedFormModelType()
                },
                it.constraints,
                it.multiplicity,
            )
        }
    }

    fun executeCommand(proQLCommand: ProQLCommand): Map<String, Any> {
        return restDataServiceCalls.execute(proQLCommand)
    }
}