package com.github.bkmbigo.visiozoezi.common.data.network.utils

import com.github.bkmbigo.visiozoezi.common.data.network.models.ApiError
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.util.InternalAPI
import io.ktor.utils.io.ByteReadChannel
import kotlinx.serialization.decodeFromString

@OptIn(InternalAPI::class)
suspend fun <T : Any> safeApiCall(apiCall: suspend () -> T): NetworkResult<T> {
    return try {
        NetworkResult.Success(data = apiCall.invoke())
    } catch (e: RedirectResponseException) { // 3xx errors
        val networkError = getError(responseContent = e.response.content)

        NetworkResult.Error(
            errorCode = networkError.statusCode ?: e.response.status.value,
            errorMessage = networkError.statusMessage ?: e.message
        )
    } catch (e: ClientRequestException) { // 4xx errors
        val networkError = getError(responseContent = e.response.content)

        NetworkResult.Error(
            errorCode = networkError.statusCode ?: e.response.status.value,
            errorMessage = networkError.statusMessage ?: e.message
        )
    } catch (e: ServerResponseException) { // 5xx errors
        val networkError = getError(responseContent = e.response.content)

        NetworkResult.Error(
            errorCode = networkError.statusCode ?: e.response.status.value,
            errorMessage = networkError.statusMessage ?: e.message
        )
    } catch (e: Exception) {
        NetworkResult.Error(
            errorCode = 0,
            errorMessage = e.message ?: "An unknown error occurred"
        )
    }
}

suspend fun getError(responseContent: ByteReadChannel): ApiError {
    return kotlinx.serialization.json.Json.decodeFromString(string = responseContent.toString())
}

fun <T> NetworkResult<List<T>>.toElements(): List<T>{
    return when(this){
        is NetworkResult.Success -> {
            this.data
        }
        is NetworkResult.Error -> {
            emptyList() // Error handling to be added later
        }
    }
}