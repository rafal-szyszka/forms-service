package com.prodactivv.formsservice.api.commands.services

import com.prodactivv.formsservice.api.commands.models.FieldWithDataDto
import com.prodactivv.formsservice.api.commands.models.FormWithDataDTO
import com.prodactivv.formsservice.communication.DataServiceBridgeService
import com.prodactivv.formsservice.core.data.models.Field
import com.prodactivv.formsservice.core.data.models.Form
import com.prodactivv.formsservice.core.data.repos.FormRepository
import com.prodactivv.formsservice.core.proql.models.ProQLQuery
import org.springframework.stereotype.Service
import java.util.*

@Service
class FormQueryService(
    private val dataServiceBridgeService: DataServiceBridgeService,
    private val formRepository: FormRepository
) {
    fun getFormWithData(id: String, dataId: String): FormWithDataDTO? {
        val form = formRepository.findById(id).get()
        val data = dataServiceBridgeService.getData(getFormProQLQuery(form, dataId))[0]

        return form.fields?.let {
            FormWithDataDTO(
                form.name!!,
                form.type!!,
                it.map { FieldWithDataDto(
                    it.label!!,
                    it.persistenceData!!,
                    it.decorators,
                    getFieldValue(it, data)
                ) }
            )
        }
    }

    private fun getFormProQLQuery(form: Form, dataId: String): ProQLQuery {
        return ProQLQuery(
            form.type.toString(),
            mutableMapOf("id" to dataId),
            null
        )
    }

    private fun getFieldValue(formField: Field, dataFields: Map<String, Any>): Any? {

        return dataFields[formField.persistenceData!!.name]
    }

    fun getForm(id: String): Optional<Form> {
        return formRepository.findById(id)
    }
}