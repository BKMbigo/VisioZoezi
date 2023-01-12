package com.github.bkmbigo.visiozoezi.common.data.network.api

import com.github.bkmbigo.visiozoezi.common.data.network.utils.logLogger
import com.github.bkmbigo.visiozoezi.common.data.network.utils.logResponse
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.http.ContentType
import io.ktor.http.ContentType.Application.Json
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

expect val ktorClient: HttpClient

fun <T : HttpClientEngineConfig> HttpClientConfig<T>.installContentNegotiation() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        }, contentType = ContentType.Any)
    }
}

fun <T: HttpClientEngineConfig> HttpClientConfig<T>.installLogging(){
    install(Logging) {
        logger = object: Logger {
            override fun log(message: String) {
                logLogger(message)
            }
        }
    }
}

fun <T: HttpClientEngineConfig> HttpClientConfig<T>.installResponseObserver(){
    install(ResponseObserver){
        onResponse {
            logResponse(it.status.value, it.status, it.call.toString())
        }
    }
}