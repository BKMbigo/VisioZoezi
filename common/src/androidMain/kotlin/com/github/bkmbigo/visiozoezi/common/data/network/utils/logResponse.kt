package com.github.bkmbigo.visiozoezi.common.data.network.utils

import android.util.Log
import io.ktor.http.HttpStatusCode

actual fun logResponse(
    responseCode: Int,
    responseStatus: HttpStatusCode,
    responseCall: String
) {
    Log.v("Ktor Client", "Ktor Client Response Code ==> $responseCode")
    Log.v("Ktor Client", "Ktor Client Response Status ==> $responseStatus")
    Log.v("Ktor Client", "Ktor Client Response Call ==> $responseCall")
}

actual fun logLogger(message: String) {
    Log.v("Ktor Client", "Ktor Client Logger ==> $message")
}