package com.prodactivv.formsservice.api.exposed

import com.prodactivv.formsservice.api.commands.models.FormDto
import com.prodactivv.formsservice.api.commands.services.FormCommandService
import com.prodactivv.formsservice.api.commands.services.FormPersistenceService
import com.prodactivv.formsservice.core.data.models.Form
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["v1/commands"])
class FormsCommandsController(
        private val formCommandService: FormCommandService,
        private val formPersistenceService: FormPersistenceService,
) {

    @PostMapping("/{formId}")
    fun execute(@RequestBody data: Map<String, Any>, @PathVariable formId: String): Map<String, Any> {
        return formPersistenceService.save(data, formId)
    }

    @PostMapping
    fun createForm(@RequestBody form: FormDto): ResponseEntity<Form> {
        val formSaved = formCommandService.createForm(form)
        return if (formSaved.isPresent) {
            ResponseEntity.ok(formSaved.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

}