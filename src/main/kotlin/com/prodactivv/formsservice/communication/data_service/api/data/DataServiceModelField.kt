package com.prodactivv.formsservice.communication.data_service.api.data

import com.prodactivv.formsservice.communication.models.Constraints

class DataServiceModelField(
    val name: String,
    val type: String,
    val constraints: List<Constraints>,
    val multiplicity: String,
) {
}


class ModelField(
    val name: String,
    val type: FormModel,
    val constraints: List<Constraints>,
    val multiplicity: String,
) {

    fun typeString(): String {
        return "${type.module}.${type.name}"
    }

}