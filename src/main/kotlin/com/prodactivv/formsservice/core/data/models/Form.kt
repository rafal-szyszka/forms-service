package com.prodactivv.formsservice.core.data.models

import com.prodactivv.formsservice.communication.data_service.api.data.FormModel
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
open class Form(
    @Id
    open var id: String? = null,

    open var name: String? = null,

    open var type: FormModel? = null,

    @org.springframework.data.mongodb.core.mapping.Field("fields")
    @DBRef
    open var fields: List<Field>? = null,
)