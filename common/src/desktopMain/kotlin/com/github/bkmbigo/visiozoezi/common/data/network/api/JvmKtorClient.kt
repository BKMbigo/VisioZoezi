package com.github.bkmbigo.visiozoezi.common.data.network.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.java.Java

actual val ktorClient: HttpClient = HttpClient(Java){
    installContentNegotiation()

    installLogging()

    installResponseObserver()
}