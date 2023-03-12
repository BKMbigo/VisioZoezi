package com.github.bkmbigo.visiozoezi.common.data.network.api

import io.ktor.client.*
import io.ktor.client.engine.js.*

actual val ktorClient: HttpClient = HttpClient(Js){
    installContentNegotiation()
    installLogging()
    installResponseObserver()
}