package com.github.bkmbigo.visiozoezi.common.data.network.utils

import io.ktor.http.HttpStatusCode

actual fun logResponse(
    responseCode: Int,
    responseStatus: HttpStatusCode,
    responseCall: String
) {
}

actual fun logLogger(message: String) {
}