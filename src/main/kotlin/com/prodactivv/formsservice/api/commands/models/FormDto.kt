package com.prodactivv.formsservice.api.commands.models

import com.prodactivv.formsservice.communication.models.FormModel

class FormDto(
    val name: String,
    val type: FormModel,
    val fields: List<FieldDto>
) {
}