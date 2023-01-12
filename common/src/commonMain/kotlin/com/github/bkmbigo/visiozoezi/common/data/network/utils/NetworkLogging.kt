package com.github.bkmbigo.visiozoezi.common.data.network.utils

import io.ktor.http.HttpStatusCode


expect fun logResponse(responseCode: Int, responseStatus: HttpStatusCode, responseCall: String)

expect fun logLogger(message: String)