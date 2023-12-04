package com.prodactivv.formsservice.communication.data_service.api.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource(value = ["classpath:DataService.properties"])
class MultiValueFieldsConfig {

    @Value("\${multiValueFields.host.url}")
    lateinit var multiValueFieldsUrl: String

    @Value("\${multiValueFields.ttl}")
    lateinit var multiValueFieldsTTL: String

}