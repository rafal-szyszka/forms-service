package com.prodactivv.formsservice.api.exposed

import com.prodactivv.formsservice.communication.data_service.api.DataServiceBridgeService
import com.prodactivv.formsservice.communication.data_service.api.data.DataServiceModelField
import com.prodactivv.formsservice.communication.data_service.api.data.FormModel
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