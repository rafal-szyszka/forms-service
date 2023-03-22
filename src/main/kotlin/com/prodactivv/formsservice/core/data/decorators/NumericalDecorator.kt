package com.prodactivv.formsservice.core.data.decorators

class NumericalDecorator: AbstractDecorator() {

    override fun check(value: String): Boolean {
        val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
        return value.matches(regex)
    }

}