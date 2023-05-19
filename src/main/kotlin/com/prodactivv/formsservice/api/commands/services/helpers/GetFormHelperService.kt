package com.prodactivv.formsservice.api.commands.services.helpers

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
class GetFormHelperService(
    private val dataServiceBridgeService: DataServiceBridgeService,
    private val formRepository: FormRepository
) {
    fun getFormWithData(formId: String, dataId: String): FormWithDataDTO? {
        val form = getFormStructure(formId)
        val data = dataServiceBridgeService.getData(getFormProQLQuery(form, dataId))[0]

        return FormWithDataDTO(
            form.name!!,
            form.type!!,
            getFields(form.fields, data)
        )
    }

    private fun getFormStructure(id: String): Form {
        val formOptional: Optional<Form> = formRepository.findById(id)
        if (!(formOptional.isPresent)) {
            println("Form not found")
        }
        return formOptional.get()
    }

    private fun getFormProQLQuery(form: Form, dataId: String): ProQLQuery {
        return ProQLQuery(
            form.type.toString(),
            mutableMapOf("id" to dataId),
            null
        )
    }

    private fun getFields(formFields: List<Field>?, dataFields: Map<String, Any>): List<FieldWithDataDto> {
        val fieldsWithDataDto: MutableList<FieldWithDataDto> = mutableListOf()
        for (field in formFields!!) {
            fieldsWithDataDto.add(
                FieldWithDataDto(
                    field.label!!,
                    field.persistenceData!!,
                    field.decorators,
                    getFieldValue(field, dataFields)
                )
            )
        }
        return fieldsWithDataDto;
    }

    private fun getFieldValue(formField: Field, dataFields: Map<String, Any>): Any? {

        return dataFields[formField.persistenceData!!.name]
    }

    fun getForm(id: String): Optional<Form> {
        return formRepository.findById(id)
    }
}