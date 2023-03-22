package com.prodactivv.formsservice.api.commands.exceptions

import java.util.function.Supplier

class UnknownFormExceptionSupplier: Supplier<Throwable> {
    override fun get(): Throwable {
        return UnknownFormException()
    }

}

class UnknownFormException: Throwable() {}
