package com.prodactivv.formsservice.api.commands.services

import com.prodactivv.formsservice.api.commands.exceptions.UnknownFormExceptionSupplier
import com.prodactivv.formsservice.communication.data_service.api.DataServiceBridgeService
import com.prodactivv.formsservice.communication.data_service.api.data.ModelField
import com.prodactivv.formsservice.core.data.models.Field
import com.prodactivv.formsservice.core.data.models.Form
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
        val rootCommand = ProQLCommand(
            form.type.toString(),
            mutableMapOf(),
            mutableListOf(),
        )
        form.fields?.forEach {
            val persistenceData = it.persistenceData
            val type = persistenceData!!.type
            if (type.isPrimitive()) {
                data[it.id]?.let { value -> rootCommand.properties.put(persistenceData.name, value) }
            } else {
                data[it.id]?.let { value -> handleComplexType(value, persistenceData, form, it, rootCommand) }
            }
        }

        return rootCommand
    }

    private fun handleComplexType(
        fieldValue: Any?,
        persistenceData: ModelField,
        form: Form,
        field: Field,
        rootCommand: ProQLCommand
    ) {
        fieldValue?.let {
            val adjustedValue = if (persistenceData.constraints.any { it.name == "SaveByText" }) {
                adjustValueBySaveByTextConstraint(fieldValue, persistenceData, form, field)
            } else {
                fieldValue
            }

            rootCommand.commands.add(
                ProQLCommand(
                    type = persistenceData.type.toString(),
                    properties = mutableMapOf("id" to adjustedValue),
                    commands = mutableListOf(),
                    parentProperty = persistenceData.name
                )
            )
        }
    }

    private fun adjustValueBySaveByTextConstraint(
        fieldValue: Any,
        persistenceData: ModelField,
        form: Form,
        field: Field,
    ): Any {
        val attribute = persistenceData.constraints.find { it.name == "SaveByText" }?.attribute ?: return fieldValue
        val queryType = "${form.type!!.module}.${field.label}"
        return dataServiceBridgeService.getData(
            ProQLQuery(
                queryType, mutableMapOf(attribute to fieldValue), null
            )
        ).firstOrNull()?.get("id") ?: fieldValue
    }

}
