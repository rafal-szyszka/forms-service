package com.prodactivv.formsservice.communication.models

const val PRIMITIVES = "primitives"

class FormModel(
    val module: String,
    val name: String,
) {

    companion object {
        fun of(type: String): FormModel {
            val formModel = type.split(".")
            return FormModel(formModel[0], formModel[1])
        }
    }

    fun isPrimitive(): Boolean {
        return module == PRIMITIVES
    }

    override fun toString(): String {
        return "$module.$name"
    }


}