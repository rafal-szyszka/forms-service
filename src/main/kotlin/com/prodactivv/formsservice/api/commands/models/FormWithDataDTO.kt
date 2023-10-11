package com.prodactivv.formsservice.api.commands.models

import com.prodactivv.formsservice.communication.data_service.api.data.FormModel

class FormWithDataDTO (
    val name: String,
    val type: FormModel,
    val fields: List<FieldWithDataDto>
    )
