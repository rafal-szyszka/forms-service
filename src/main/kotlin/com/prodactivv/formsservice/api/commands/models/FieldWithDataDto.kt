package com.prodactivv.formsservice.api.commands.models

import com.prodactivv.formsservice.communication.data_service.api.data.ModelField
import com.prodactivv.formsservice.core.data.models.Decorator

class FieldWithDataDto(
    override val label: String,
    override val modelField: ModelField,
    override val decorators: List<Decorator>? = null,
    val value: Any?
): FieldDto(label, modelField, decorators) {
}