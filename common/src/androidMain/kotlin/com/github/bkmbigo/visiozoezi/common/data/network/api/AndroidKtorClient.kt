package com.github.bkmbigo.visiozoezi.common.data.network.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android

actual val ktorClient: HttpClient = HttpClient(Android){

    installContentNegotiation()

    installLogging()

    installResponseObserver()
}