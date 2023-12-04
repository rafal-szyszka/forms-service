package com.prodactivv.formsservice.communication.data_service.api.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource("classpath:DataService.properties")
@PropertySource(value = ["classpath:DataService-\${spring.profiles.active}.properties"], ignoreResourceNotFound = true)
class DataServiceRestConfiguration {

    @Value("\${dataService.host.url}")
    lateinit var dataServiceHost: String

    @Value("\${dataService.endpoints.getModels}")
    lateinit var getModelsEndpoint: String

    @Value("\${dataService.endpoints.getModel}")
    lateinit var getModelEndpoint: String

    @Value("\${dataService.endpoints.executeCommand}")
    lateinit var executeCommandEndpoint: String

    @Value("\${dataService.endpoints.load}")
    lateinit var loadEndpoint: String

}