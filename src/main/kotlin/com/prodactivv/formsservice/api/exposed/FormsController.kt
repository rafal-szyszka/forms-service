package com.prodactivv.formsservice.api.exposed

import com.prodactivv.formsservice.api.commands.models.FormDto
import com.prodactivv.formsservice.api.commands.services.FormCommandService
import com.prodactivv.formsservice.api.commands.models.FormWithDataDTO
import com.prodactivv.formsservice.api.commands.services.FormQueryService
import com.prodactivv.formsservice.core.data.models.Form
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(value = ["v1/forms"])
class FormsController(
    private val formCommandService: FormCommandService,
    private val formQueryService: FormQueryService,
) {

    @GetMapping("/{id}/{dataId}")
    fun getFormWithData(@PathVariable id: String, @PathVariable dataId: String): ResponseEntity<FormWithDataDTO> {
        val formWithData = formQueryService.getFormWithData(id, dataId)
        return if (formWithData.isPresent) {
            ResponseEntity.ok(formWithData.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/{id}")
    fun getForm(@PathVariable id: String): ResponseEntity<Form> {
        val form = formQueryService.getForm(id)
        return if (form.isPresent) {
            ResponseEntity.ok(form.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun createForm(@RequestBody form: FormDto): ResponseEntity<Form> {
        val formSaved =  formCommandService.createForm(form)
        return if (formSaved.isPresent) {
            ResponseEntity.ok(formSaved.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

}