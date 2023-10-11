package com.prodactivv.formsservice.core.data.models

import com.fasterxml.jackson.annotation.JsonInclude
import com.prodactivv.formsservice.communication.data_service.api.data.ModelField
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
@JsonInclude(JsonInclude.Include.NON_NULL)
open class Field(

    @Id
    open var id: String? = null,

    open var label: String? = null,

    open var decorators: List<Decorator>? = null,

    open var dataUrl: String? = null,

    open var updateUrl: String? = null,

    open var persistenceData: ModelField? = null,
)