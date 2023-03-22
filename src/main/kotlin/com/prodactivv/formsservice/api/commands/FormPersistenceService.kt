package com.prodactivv.formsservice.api.commands

import com.prodactivv.formsservice.api.commands.exceptions.UnknownFormExceptionSupplier
import com.prodactivv.formsservice.communication.DataServiceBridgeService
import com.prodactivv.formsservice.core.data.repos.FormRepository
import com.prodactivv.formsservice.core.proql.models.ProQLCommand
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
                val any = data[it.id]
                if (any != null) {
                    root.commands.add(
                        ProQLCommand(
                            type = persistenceData.type.toString(),
                            properties = mutableMapOf(Pair("id", any)),
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
