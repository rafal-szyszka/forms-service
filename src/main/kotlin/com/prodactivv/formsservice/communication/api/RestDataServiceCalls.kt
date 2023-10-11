package com.prodactivv.formsservice.communication.api

import com.prodactivv.formsservice.communication.models.FormModel
import com.prodactivv.formsservice.communication.models.DataServiceModelField
import com.prodactivv.formsservice.core.data.metadata.MetaDataCacheLoader
import com.prodactivv.formsservice.core.proql.models.ProQLCommand
import com.prodactivv.formsservice.core.proql.models.ProQLQuery
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component

@Component
@PropertySource(value = ["classpath:DataService.properties"])
class RestDataServiceCalls {

    @Value("\${dataService.host.url}")
    private lateinit var dataServiceHost: String

    @Value("\${dataService.endpoints.getModels}")
    private lateinit var getModelsEndpoint: String

    @Value("\${dataService.endpoints.getModel}")
    private lateinit var getModelEndpoint: String

    @Value("\${dataService.endpoints.executeCommand}")
    private lateinit var executeCommandEndpoint: String

    @Value("\${dataService.endpoints.load}")
    private lateinit var loadEndpoint: String

    private var httpClient: HttpClient = createClient()

    @Autowired
    private lateinit var metaDataCacheLoader: MetaDataCacheLoader

    fun getModels(): List<String> = runBlocking {
        return@runBlocking httpClient.get(dataServiceHost) {
            url {
                appendPathSegments(getModelsEndpoint)
            }
        }.body<List<String>>()
    }

    fun getData(proQLQuery: ProQLQuery): List<Map<String, Any>> = runBlocking {
        return@runBlocking httpClient.post(dataServiceHost) {
            url {
                appendPathSegments(loadEndpoint)
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
        val cachedModelFields = metaDataCacheLoader.getModelDetailsFromCache(model)
        if (!cachedModelFields.isNullOrEmpty()) {
            return cachedModelFields
        }
        return runBlocking {
            httpClient.get(dataServiceHost) {
                url {
                    appendPathSegments(getModelEndpoint, "${model.module}.${model.name}")
                }
            }.body<List<DataServiceModelField>>()
        }
    }

    fun execute(proQLCommand: ProQLCommand): Map<String, Any> = runBlocking {
        return@runBlocking httpClient.post(dataServiceHost) {
            url {
                appendPathSegments(executeCommandEndpoint)
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