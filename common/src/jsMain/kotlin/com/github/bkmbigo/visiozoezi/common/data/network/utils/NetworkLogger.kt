package com.github.bkmbigo.visiozoezi.common.data.network.utils

import io.ktor.http.*

actual fun logResponse(
    responseCode: Int,
    responseStatus: HttpStatusCode,
    responseCall: String
) {
    console.log("Response Obtained: Code: $responseCode, Status: $responseStatus, Call: $responseCall")
}

actual fun logLogger(message: String) {
    console.log("Network Log: $message")
}