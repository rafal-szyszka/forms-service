package com.prodactivv.formsservice.core.data.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.prodactivv.formsservice.communication.models.FormModel
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
open class Form(
    @Id
    open var id: String? = null,

    open var name: String? = null,

    @JsonIgnore
    open var type: FormModel? = null,

    @org.springframework.data.mongodb.core.mapping.Field("fields")
    @DBRef
    open var fields: List<Field>? = null,
)