package com.prodactivv.formsservice.communication.models

class DataServiceModelField(
    val name: String,
    val type: String,
    val constraints: List<String>,
    val multiplicity: String,
) {
}

class ModelField(
    val name: String,
    val type: FormModel,
    val constraints: List<String>,
    val multiplicity: String,
) {

    fun typeString(): String {
        return "${type.module}.${type.name}"
    }

}