package com.prodactivv.formsservice.api.exposed

import com.prodactivv.formsservice.communication.DataServiceBridgeService
import com.prodactivv.formsservice.communication.models.DataServiceModelField
import com.prodactivv.formsservice.communication.models.FormModel
import com.prodactivv.formsservice.communication.models.ModelField
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["v1/meta"])
class FormModelsController(
    private val dataServiceBridgeService: DataServiceBridgeService
) {

    @GetMapping()
    fun getModels(): List<FormModel> {
        return dataServiceBridgeService.getModels()
    }

    @GetMapping("/{module}")
    fun getModels(@PathVariable module: String): List<FormModel> {
        return dataServiceBridgeService.getModels(module)
    }

    @GetMapping("/{module}/{model}")
    fun getModel(@PathVariable module: String, @PathVariable model: String): List<DataServiceModelField> {
        return dataServiceBridgeService.getModel(module, model)
    }

}