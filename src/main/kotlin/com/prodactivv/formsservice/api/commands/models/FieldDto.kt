package com.prodactivv.formsservice.api.commands.models

import com.prodactivv.formsservice.communication.data_service.api.data.ModelField
import com.prodactivv.formsservice.core.data.models.Decorator

open class FieldDto(
    open val label: String,
    open val modelField: ModelField,
    open val decorators: List<Decorator>? = null
) {
}