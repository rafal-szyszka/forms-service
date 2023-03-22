package com.prodactivv.formsservice.communication

import com.prodactivv.formsservice.communication.api.RestDataServiceCalls
import com.prodactivv.formsservice.communication.exceptions.UnsupportedFormModelType
import com.prodactivv.formsservice.communication.models.FormModel
import com.prodactivv.formsservice.communication.models.DataServiceModelField
import com.prodactivv.formsservice.communication.models.ModelField
import com.prodactivv.formsservice.communication.models.PRIMITIVES
import com.prodactivv.formsservice.core.proql.models.ProQLCommand
import org.springframework.stereotype.Service

@Service
class DataServiceBridgeService(
    private val restDataServiceCalls: RestDataServiceCalls
) {

    fun getModels(): List<FormModel> {
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

    fun getModel(module: String, model: String): List<ModelField> {
        return restDataServiceCalls.getModel(FormModel(module, model))
            .map {
                val formModel = it.type.split(".")
                ModelField(
                    it.name,
                    when (formModel.size) {
                        1 -> FormModel(PRIMITIVES, it.type)
                        2 -> FormModel(formModel[0], formModel[1])
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