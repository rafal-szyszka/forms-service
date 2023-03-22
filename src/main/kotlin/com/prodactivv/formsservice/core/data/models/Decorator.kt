package com.prodactivv.formsservice.core.data.models

import com.fasterxml.jackson.annotation.JsonIgnore

open class Decorator (
    open var name: String? = null,

    @JsonIgnore
    open var value: String? = null

)