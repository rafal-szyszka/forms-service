package com.prodactivv.formsservice.api.commands.services

import com.prodactivv.formsservice.api.commands.exceptions.UnknownFormExceptionSupplier
import com.prodactivv.formsservice.communication.data_service.api.DataServiceBridgeService
import com.prodactivv.formsservice.core.data.repos.FormRepository
import com.prodactivv.formsservice.core.proql.models.ProQLCommand
import com.prodactivv.formsservice.core.proql.models.ProQLQuery
import org.springframework.stereotype.Service

@Service
class FormPersistenceService(
    private val formRepository: FormRepository,
    private val dataServiceBridgeService: DataServiceBridgeService,
) {

    fun save(data: Map<String, Any>, formId: String): Map<String, Any> {
        val proQLCommand = createProQLCommand(data, formId)
        return dataServiceBridgeService.executeCommand(proQLCommand)
    }

    private fun createProQLCommand(data: Map<String, Any>, formId: String): ProQLCommand {
        val form = formRepository.findById(formId).orElseThrow(UnknownFormExceptionSupplier())
        val root = ProQLCommand(
            form.type.toString(),
            mutableMapOf(),
            mutableListOf(),
        )
        form.fields?.forEach {
            val persistenceData = it.persistenceData
            val type = persistenceData!!.type
            if (type.isPrimitive()) {
                data[it.id]?.let { value -> root.properties.put(persistenceData.name, value) }
            } else {
                var any = data[it.id]
                if (any != null) {
                    if(persistenceData!!.constraints.contains("SaveByText")){
                        val toChangeIds = dataServiceBridgeService.getData(ProQLQuery(it.label.toString(),
                                mutableMapOf(Pair(persistenceData.name, any)), null))
                        any = toChangeIds[0]["id"]
                    }
                    root.commands.add(
                        ProQLCommand(
                            type = persistenceData.type.toString(),
                            properties = mutableMapOf(Pair("id", any.toString())),
                            commands = mutableListOf(),
                            parentProperty = persistenceData.name
                        )
                    )
                }
            }
        }

        return root
    }

}
