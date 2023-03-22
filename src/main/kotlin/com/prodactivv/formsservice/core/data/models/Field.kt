package com.prodactivv.formsservice.core.data.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.prodactivv.formsservice.communication.models.ModelField
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
open class Field(

    @Id
    open var id: String? = null,

    open var label: String? = null,

    open var decorators: List<Decorator>? = null,

    @JsonIgnore
    open var persistenceData: ModelField? = null,
)