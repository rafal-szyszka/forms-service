package com.prodactivv.formsservice.communication.data_service.api.data

import com.fasterxml.jackson.annotation.JsonProperty

class DataServiceModelField(
    val name: String,
    val type: String,
    val constraints: List<String>,
    val multiplicity: String,
) {
}


class ModelField(
    val name: String,
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    val type: FormModel,
    val constraints: List<String>,
    val multiplicity: String,
) {

    fun typeString(): String {
        return "${type.module}.${type.name}"
    }

}