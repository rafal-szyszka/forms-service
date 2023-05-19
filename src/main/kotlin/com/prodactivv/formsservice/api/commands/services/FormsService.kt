package com.prodactivv.formsservice.api.commands.services

import com.prodactivv.formsservice.api.commands.models.FormDto
import com.prodactivv.formsservice.api.commands.models.FormWithDataDTO
import com.prodactivv.formsservice.api.commands.services.helpers.CreateFormHelperService
import com.prodactivv.formsservice.api.commands.services.helpers.GetFormHelperService
import com.prodactivv.formsservice.core.data.models.Form
import org.springframework.stereotype.Service

@Service
class FormsService(
    private val getFormHelperService: GetFormHelperService,
    private val createFormHelperService: CreateFormHelperService,
) {
    fun createForm(formDto: FormDto): Form {
        return createFormHelperService.createForm(formDto)
    }

    fun getForm(formId: String, dataId: String): FormWithDataDTO? {
        return getFormHelperService.getFormWithData(formId, dataId)
    }

}