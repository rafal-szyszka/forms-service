package com.prodactivv.formsservice.api.exposed

import com.prodactivv.formsservice.api.commands.models.FormDto
import com.prodactivv.formsservice.api.commands.services.FormsService
import com.prodactivv.formsservice.api.commands.models.FormWithDataDTO
import com.prodactivv.formsservice.core.data.models.Form
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(value = ["forms"])
class FormsController(
    private val formsService: FormsService,
) {

    @GetMapping("/{formId}/{dataId}")
    fun getForm(@PathVariable formId: String, @PathVariable dataId: String): FormWithDataDTO? {
        return formsService.getForm(formId, dataId)
    }

    @PostMapping
    fun createForm(@RequestBody form: FormDto): Form {
        return formsService.createForm(form)
    }

}