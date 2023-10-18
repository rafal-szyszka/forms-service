package com.prodactivv.formsservice.communication.data_service.api

import com.prodactivv.formsservice.communication.data_service.api.config.DataServiceRestConfiguration
import com.prodactivv.formsservice.communication.data_service.api.data.DataServiceModelField
import com.prodactivv.formsservice.communication.data_service.api.data.FormModel
import com.prodactivv.formsservice.core.proql.models.ProQLCommand
import com.prodactivv.formsservice.core.proql.models.ProQLQuery
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component

@Component
class RestDataServiceCalls(
    private val configuration: DataServiceRestConfiguration
) {

    private var httpClient: HttpClient = createClient()

    fun getModels(): List<String> = runBlocking {
        return@runBlocking httpClient.get(configuration.dataServiceHost) {
            url {
                appendPathSegments(configuration.getModelsEndpoint)
            }
        }.body<List<String>>()
    }

    fun getData(proQLQuery: ProQLQuery): List<Map<String, Any>> = runBlocking {
        return@runBlocking httpClient.post(configuration.dataServiceHost) {
            url {
                appendPathSegments(configuration.loadEndpoint)
            }
            setBody(proQLQuery)
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }.body<List<Map<String, Any>>>()
    }

    fun getModels(module: String): List<String> = runBlocking {
        throw NotImplementedError()
    }

    fun getModel(model: FormModel): List<DataServiceModelField> {
        return runBlocking {
            httpClient.get(configuration.dataServiceHost) {
                url {
                    appendPathSegments(configuration.getModelEndpoint, "${model.module}.${model.name}")
                }
            }.body<List<DataServiceModelField>>()
        }
    }

    fun execute(proQLCommand: ProQLCommand): Map<String, Any> = runBlocking {
        return@runBlocking httpClient.post(configuration.dataServiceHost) {
            url {
                appendPathSegments(configuration.executeCommandEndpoint)
            }
            setBody(proQLCommand)
            contentType(ContentType.Application.Json)
        }.body<Map<String, Any>>()
    }

    private fun createClient(): HttpClient {
        return HttpClient(CIO) {
            install(DefaultRequest)
            install(Logging)
            install(ContentNegotiation) {
                jackson()
            }
        }
    }
}