package com.prodactivv.formsservice.api.commands.models

import com.prodactivv.formsservice.communication.models.ModelField
import com.prodactivv.formsservice.core.data.models.Decorator

class FieldDto(
    val label: String,
    val modelField: ModelField,
    val decorators: List<Decorator>? = null
) {
}