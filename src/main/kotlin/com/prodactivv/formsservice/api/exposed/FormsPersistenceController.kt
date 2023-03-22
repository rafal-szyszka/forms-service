package com.prodactivv.formsservice.api.exposed

import com.prodactivv.formsservice.api.commands.FormPersistenceService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["forms/persistence"])
class FormsPersistenceController(
    private val formPersistenceService: FormPersistenceService,
) {

    @PostMapping("/{formId}")
    fun execute(@RequestBody data: Map<String, Any>, @PathVariable formId: String): Map<String, Any> {
        return formPersistenceService.save(data, formId)
    }

}