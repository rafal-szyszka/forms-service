package com.prodactivv.formsservice.api.exposed

import com.prodactivv.formsservice.api.commands.models.FormDto
import com.prodactivv.formsservice.api.commands.FormsService
import com.prodactivv.formsservice.communication.DataServiceBridgeService
import com.prodactivv.formsservice.communication.models.FormModel
import com.prodactivv.formsservice.core.data.models.Form
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["forms"])
class FormsController(
    private val formsService: FormsService,
) {

    @GetMapping("/{id}")
    fun getForm(@PathVariable id: Long): Any {
        return Any()
    }

    @PostMapping
    fun createForm(@RequestBody form: FormDto): Form {
        return formsService.createForm(form)
    }

}